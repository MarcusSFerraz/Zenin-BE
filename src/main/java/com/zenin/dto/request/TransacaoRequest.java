package com.zenin.dto.request;

import com.zenin.model.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransacaoRequest(
        UUID categoriaId,
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        UUID transacaoRecorrenteId,
        @NotNull(message = "Tipo é obrigatório") TipoTransacao tipo,
        @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor,
        String descricao,
        LocalDate data,
        LocalDate dataVencimento,
        boolean pago
) {}
