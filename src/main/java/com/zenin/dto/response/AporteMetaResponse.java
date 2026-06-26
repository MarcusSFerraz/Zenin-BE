package com.zenin.dto.response;

import com.zenin.model.AporteMeta;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AporteMetaResponse(
        UUID id,
        UUID metaId,
        BigDecimal valor,
        CarteiraResumo carteira,
        OffsetDateTime criadoEm
) {
    public static AporteMetaResponse from(AporteMeta a) {
        return new AporteMetaResponse(
                a.getId(), a.getMeta().getId(), a.getValor(),
                CarteiraResumo.from(a.getCarteira()), a.getCriadoEm()
        );
    }
}
