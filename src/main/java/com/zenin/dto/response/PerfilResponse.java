package com.zenin.dto.response;

import com.zenin.model.User;

import java.util.UUID;

public record PerfilResponse(
        UUID id,
        String email,
        String nome,
        String urlAvatar,
        String preferenciaTema,
        Integer diaPagamento,
        String numeroWhatsapp,
        Boolean ativo,
        String role
) {
    public static PerfilResponse from(User u) {
        return new PerfilResponse(
                u.getId(), u.getEmail(), u.getName(),
                u.getUrlAvatar(), u.getPreferenciaTema(),
                u.getDiaPagamento(), u.getNumeroWhatsapp(),
                u.getAtivo(), u.getRole().name()
        );
    }
}
