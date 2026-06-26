package com.zenin.dto.response;

import com.zenin.model.FaturaCartao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FaturaCartaoResponse(
        UUID id,
        BigDecimal valor,
        LocalDate dataVencimento,
        boolean pago,
        CarteiraResumo carteira,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public static FaturaCartaoResponse from(FaturaCartao f) {
        return new FaturaCartaoResponse(
                f.getId(), f.getValor(), f.getDataVencimento(), f.isPago(),
                CarteiraResumo.from(f.getCarteira()),
                f.getCriadoEm(), f.getAtualizadoEm()
        );
    }
}
