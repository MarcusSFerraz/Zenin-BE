package com.zenin.dto.response;

import com.zenin.model.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioAdminResponse(
        UUID id,
        String email,
        String nome,
        String urlAvatar,
        Boolean ativo,
        String role,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public static UsuarioAdminResponse from(User u) {
        return new UsuarioAdminResponse(
                u.getId(), u.getEmail(), u.getName(),
                u.getUrlAvatar(), u.getAtivo(), u.getRole().name(),
                u.getCriadoEm(), u.getAtualizadoEm()
        );
    }
}
