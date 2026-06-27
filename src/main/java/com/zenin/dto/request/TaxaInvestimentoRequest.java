package com.zenin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxaInvestimentoRequest(
        @NotBlank(message = "Tipo é obrigatório") String tipo,
        String nome,
        String referencia,
        BigDecimal taxaAnual,
        @NotNull(message = "Taxa diária é obrigatória") BigDecimal taxaDiaria,
        BigDecimal puVenda,
        BigDecimal puBase,
        LocalDate vencimento,
        @NotNull(message = "Data de referência é obrigatória") LocalDate dataReferencia
) {}
