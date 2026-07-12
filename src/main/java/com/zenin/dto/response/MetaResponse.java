package com.zenin.dto.response;

import com.zenin.model.Meta;
import com.zenin.model.TipoPeriodo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MetaResponse(
        UUID id,
        UUID investimentoId,
        String nome,
        BigDecimal valorAlvo,
        BigDecimal valorAtual,
        double percentualAtingido,
        BigDecimal valorInicial,
        BigDecimal totalDepositosManual,
        BigDecimal totalInvestimento,
        TipoPeriodo periodo,
        LocalDate dataInicio,
        LocalDate dataFim,
        String cor,
        String icone,
        CategoriaResponse categoria,
        OffsetDateTime criadoEm
) {
    public static MetaResponse from(Meta m, BigDecimal totalDepositosManual, BigDecimal totalInvestimento) {
        BigDecimal valorInicial = m.getValorInicial() != null ? m.getValorInicial() : BigDecimal.ZERO;
        BigDecimal valorAtual = valorInicial.add(totalDepositosManual).add(totalInvestimento);

        double percentual = BigDecimal.ZERO.compareTo(m.getValorAlvo()) == 0 ? 0.0
                : valorAtual.divide(m.getValorAlvo(), 4, RoundingMode.HALF_UP).doubleValue() * 100;

        return new MetaResponse(
                m.getId(),
                m.getInvestimento() != null ? m.getInvestimento().getId() : null,
                m.getNome(),
                m.getValorAlvo(),
                valorAtual,
                Math.min(percentual, 100.0),
                valorInicial,
                totalDepositosManual,
                totalInvestimento,
                m.getPeriodo(),
                m.getDataInicio(),
                m.getDataFim(),
                m.getCor(),
                m.getIcone(),
                CategoriaResponse.from(m.getCategoria()),
                m.getCriadoEm()
        );
    }

    public static MetaResponse from(Meta m) {
        return from(m, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
