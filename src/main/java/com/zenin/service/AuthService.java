package com.zenin.service;

import com.zenin.dto.AuthResponse;
import com.zenin.dto.LoginRequest;
import com.zenin.dto.RegisterRequest;
import com.zenin.model.RefreshToken;
import com.zenin.model.Role;
import com.zenin.model.User;
import com.zenin.repository.UserRepository;
import com.zenin.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    static final String REFRESH_COOKIE_NAME = "refresh_token";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.jwt.refresh-expiration-remember-ms}")
    private long refreshExpirationRememberMs;

    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já está em uso");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.create(user, false);
        setRefreshCookie(response, refreshToken.getToken(), false);

        String accessToken = jwtService.generateToken(user);
        return AuthResponse.of(accessToken, user);
    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        boolean rememberMe = Boolean.TRUE.equals(request.rememberMe());
        RefreshToken refreshToken = refreshTokenService.create(user, rememberMe);
        setRefreshCookie(response, refreshToken.getToken(), rememberMe);

        String accessToken = jwtService.generateToken(user);
        return AuthResponse.of(accessToken, user);
    }

    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = extractRefreshCookie(request);
        if (tokenValue == null) {
            throw new IllegalArgumentException("Refresh token ausente");
        }

        RefreshToken old = refreshTokenService.findAndValidate(tokenValue);

        // Mantém rememberMe se o token ainda tiver mais que metade da validade longa
        boolean rememberMe = old.getExpiresAt().toEpochMilli() - System.currentTimeMillis()
                > (refreshExpirationRememberMs / 2);

        RefreshToken newToken = refreshTokenService.rotate(old, rememberMe);
        setRefreshCookie(response, newToken.getToken(), rememberMe);

        String accessToken = jwtService.generateToken(old.getUser());
        return AuthResponse.of(accessToken, old.getUser());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = extractRefreshCookie(request);
        if (tokenValue != null) {
            refreshTokenService.revoke(tokenValue);
        }
        clearRefreshCookie(response);
    }

    private void setRefreshCookie(HttpServletResponse response, String tokenValue, boolean rememberMe) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(REFRESH_COOKIE_NAME, tokenValue)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .sameSite("Strict");

        if (rememberMe) {
            builder.maxAge(Duration.ofMillis(refreshExpirationRememberMs));
        }

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .sameSite("Strict")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> REFRESH_COOKIE_NAME.equals(c.getName()))
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
