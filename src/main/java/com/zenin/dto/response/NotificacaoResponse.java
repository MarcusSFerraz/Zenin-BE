package com.zenin.dto.response;

import com.zenin.model.Notificacao;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificacaoResponse(
        UUID id, String titulo, String mensagem, boolean lida, OffsetDateTime criadoEm
) {
    public static NotificacaoResponse from(Notificacao n) {
        return new NotificacaoResponse(n.getId(), n.getTitulo(), n.getMensagem(), n.isLida(), n.getCriadoEm());
    }
}
