package com.zenin.dto;

public record AuthResponse(
        String token,
        String type,
        String email,
        String name
) {
    public static AuthResponse of(String token, String email, String name) {
        return new AuthResponse(token, "Bearer", email, name);
    }
}
