package com.zenin.dto.request;

import com.zenin.model.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotNull(message = "Tipo é obrigatório") TipoTransacao tipo,
        String cor,
        String icone
) {}
