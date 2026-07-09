package com.zenin.service;

import com.zenin.model.RefreshToken;
import com.zenin.model.User;
import com.zenin.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Value("${app.jwt.refresh-expiration-remember-ms}")
    private long refreshExpirationRememberMs;

    @Transactional
    public RefreshToken create(User user, boolean rememberMe) {
        long expirationMs = rememberMe ? refreshExpirationRememberMs : refreshExpirationMs;

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(expirationMs))
                .build();

        return refreshTokenRepository.save(token);
    }

    public RefreshToken findAndValidate(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (!token.isValid()) {
            throw new IllegalArgumentException("Refresh token expirado ou revogado");
        }

        return token;
    }

    @Transactional
    public void revoke(String tokenValue) {
        refreshTokenRepository.findByToken(tokenValue).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    @Transactional
    public void revokeAllByUser(User user) {
        refreshTokenRepository.revokeAllByUser(user);
    }

    @Transactional
    public RefreshToken rotate(RefreshToken old, boolean rememberMe) {
        old.setRevoked(true);
        refreshTokenRepository.save(old);
        return create(old.getUser(), rememberMe);
    }
}
