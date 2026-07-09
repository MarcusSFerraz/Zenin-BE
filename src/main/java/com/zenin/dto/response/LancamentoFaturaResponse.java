package com.zenin.dto.response;

import com.zenin.model.LancamentoFatura;
import com.zenin.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LancamentoFaturaResponse(
        UUID id,
        UUID faturaId,
        String descricao,
        BigDecimal valor,
        TipoLancamento tipo,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public static LancamentoFaturaResponse from(LancamentoFatura lancamento) {
        return new LancamentoFaturaResponse(
                lancamento.getId(),
                lancamento.getFatura().getId(),
                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getTipo(),
                lancamento.getCriadoEm(),
                lancamento.getAtualizadoEm()
        );
    }
}
