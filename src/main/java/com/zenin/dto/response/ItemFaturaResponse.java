package com.zenin.dto.response;

import com.zenin.model.ItemFatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ItemFaturaResponse(
        UUID id,
        String descricao,
        BigDecimal valor,
        LocalDate dataCompra,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
    public static ItemFaturaResponse from(ItemFatura item) {
        return new ItemFaturaResponse(
                item.getId(), item.getDescricao(), item.getValor(),
                item.getDataCompra(), item.getCriadoEm(), item.getAtualizadoEm()
        );
    }
}
