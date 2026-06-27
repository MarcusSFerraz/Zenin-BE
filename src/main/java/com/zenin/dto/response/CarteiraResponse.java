package com.zenin.dto.response;

import com.zenin.model.Carteira;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CarteiraResponse(
        UUID id, String nome, String cor,
        BigDecimal saldoInicial, BigDecimal saldoAtual,
        boolean cartaoCredito, boolean cartaoDebito, boolean poupanca,
        Integer diaVencimento,
        OffsetDateTime criadoEm, OffsetDateTime atualizadoEm
) {
    public static CarteiraResponse from(Carteira c) {
        return new CarteiraResponse(
                c.getId(), c.getNome(), c.getCor(),
                c.getSaldoInicial(), c.getSaldoAtual(),
                c.isCartaoCredito(), c.isCartaoDebito(), c.isPoupanca(),
                c.getDiaVencimento(),
                c.getCriadoEm(), c.getAtualizadoEm()
        );
    }
}
