package com.zenin.dto.response;

import com.zenin.model.Investimento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InvestimentoResponse(
        UUID id,
        String tipo,
        BigDecimal valorInicial,
        BigDecimal valorAtual,
        BigDecimal lucro,
        double percentualLucro,
        BigDecimal quantidadeCotas,
        LocalDate dataInicio,
        LocalDate dataVencimento,
        boolean ativo,
        CarteiraResumo carteira,
        TaxaInvestimentoResponse taxa,
        OffsetDateTime criadoEm
) {
    public static InvestimentoResponse from(Investimento i) {
        BigDecimal lucro = i.getValorAtual().subtract(i.getValorInicial());
        double percentual = BigDecimal.ZERO.compareTo(i.getValorInicial()) == 0 ? 0.0
                : lucro.divide(i.getValorInicial(), 4, RoundingMode.HALF_UP).doubleValue() * 100;

        return new InvestimentoResponse(
                i.getId(), i.getTipo(), i.getValorInicial(), i.getValorAtual(),
                lucro, percentual, i.getQuantidadeCotas(),
                i.getDataInicio(), i.getDataVencimento(), i.isAtivo(),
                CarteiraResumo.from(i.getCarteira()),
                i.getTaxa() != null ? TaxaInvestimentoResponse.from(i.getTaxa()) : null,
                i.getCriadoEm()
        );
    }
}
