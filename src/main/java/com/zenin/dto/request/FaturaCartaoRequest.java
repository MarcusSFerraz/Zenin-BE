package com.zenin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FaturaCartaoRequest(
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        @NotNull(message = "Data de vencimento é obrigatória") LocalDate dataVencimento,
        @NotNull(message = "Valor é obrigatório") @PositiveOrZero(message = "Valor não pode ser negativo") BigDecimal valor
) {}
