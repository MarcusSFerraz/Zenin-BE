package com.zenin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemFaturaRequest(
        @NotBlank(message = "Descrição é obrigatória") String descricao,
        @NotNull(message = "Valor é obrigatório") @PositiveOrZero(message = "Valor não pode ser negativo") BigDecimal valor,
        LocalDate dataCompra
) {}
