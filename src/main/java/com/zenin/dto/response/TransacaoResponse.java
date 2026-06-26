package com.zenin.dto.response;

import com.zenin.model.Transacao;
import com.zenin.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransacaoResponse(
        UUID id,
        TipoTransacao tipo,
        BigDecimal valor,
        String descricao,
        LocalDate data,
        LocalDate dataVencimento,
        boolean pago,
        OffsetDateTime pagoEm,
        CarteiraResumo carteira,
        CategoriaResponse categoria,
        UUID transacaoRecorrenteId,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public static TransacaoResponse from(Transacao t) {
        return new TransacaoResponse(
                t.getId(), t.getTipo(), t.getValor(), t.getDescricao(),
                t.getData(), t.getDataVencimento(), t.isPago(), t.getPagoEm(),
                CarteiraResumo.from(t.getCarteira()),
                CategoriaResponse.from(t.getCategoria()),
                t.getTransacaoRecorrente() != null ? t.getTransacaoRecorrente().getId() : null,
                t.getCriadoEm(), t.getAtualizadoEm()
        );
    }
}
