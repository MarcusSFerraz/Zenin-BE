package com.zenin.dto.response;

import com.zenin.model.TipoTransacao;
import com.zenin.model.TransacaoRecorrente;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransacaoRecorrenteResponse(
        UUID id,
        TipoTransacao tipo,
        BigDecimal valor,
        String nome,
        boolean ativo,
        Integer diaVencimento,
        CarteiraResumo carteira,
        CategoriaResponse categoria,
        OffsetDateTime criadoEm
) {
    public static TransacaoRecorrenteResponse from(TransacaoRecorrente t) {
        return new TransacaoRecorrenteResponse(
                t.getId(), t.getTipo(), t.getValor(), t.getNome(),
                t.isAtivo(), t.getDiaVencimento(),
                CarteiraResumo.from(t.getCarteira()),
                CategoriaResponse.from(t.getCategoria()),
                t.getCriadoEm()
        );
    }
}
