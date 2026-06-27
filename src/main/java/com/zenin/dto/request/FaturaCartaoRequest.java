package com.zenin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FaturaCartaoRequest(
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        @NotNull(message = "Mês de referência é obrigatório")
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "Formato deve ser YYYY-MM")
        String mesReferencia,
        @NotNull(message = "Data de vencimento é obrigatória") LocalDate dataVencimento,
        @NotNull(message = "Valor é obrigatório") @PositiveOrZero(message = "Valor não pode ser negativo") BigDecimal valor
) {}
