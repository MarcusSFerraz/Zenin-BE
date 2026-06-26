package com.zenin.dto.response;

import com.zenin.model.AporteInvestimento;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AporteInvestimentoResponse(
        UUID id,
        UUID investimentoId,
        BigDecimal valor,
        CarteiraResumo carteira,
        OffsetDateTime criadoEm
) {
    public static AporteInvestimentoResponse from(AporteInvestimento a) {
        return new AporteInvestimentoResponse(
                a.getId(), a.getInvestimento().getId(), a.getValor(),
                CarteiraResumo.from(a.getCarteira()), a.getCriadoEm()
        );
    }
}
