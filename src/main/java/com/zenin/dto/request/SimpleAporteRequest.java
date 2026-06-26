package com.zenin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record SimpleAporteRequest(
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor
) {}
