package com.zenin.dto;

import com.zenin.model.User;

public record AuthResponse(
        String token,
        String type,
        String email,
        String name,
        String role
) {
    public static AuthResponse of(String token, User user) {
        return new AuthResponse(token, "Bearer", user.getEmail(), user.getName(), user.getRole().name());
    }
}
