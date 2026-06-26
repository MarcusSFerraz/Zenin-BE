package com.zenin.dto.response;

import com.zenin.model.TaxaInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TaxaInvestimentoResponse(
        UUID id,
        String tipo,
        String nome,
        BigDecimal taxaAnual,
        BigDecimal taxaDiaria,
        BigDecimal puVenda,
        BigDecimal puBase,
        LocalDate vencimento,
        LocalDate dataReferencia,
        OffsetDateTime criadoEm
) {
    public static TaxaInvestimentoResponse from(TaxaInvestimento t) {
        return new TaxaInvestimentoResponse(
                t.getId(), t.getTipo(), t.getNome(), t.getTaxaAnual(), t.getTaxaDiaria(),
                t.getPuVenda(), t.getPuBase(), t.getVencimento(), t.getDataReferencia(), t.getCriadoEm()
        );
    }
}
