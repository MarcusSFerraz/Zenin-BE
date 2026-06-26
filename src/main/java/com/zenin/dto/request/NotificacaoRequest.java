package com.zenin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NotificacaoRequest(
        @NotBlank(message = "Título é obrigatório") String titulo,
        @NotBlank(message = "Mensagem é obrigatória") String mensagem
) {}
