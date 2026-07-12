package com.zenin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvestimentoRequest(
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        UUID taxaId,
        @NotBlank(message = "Tipo é obrigatório") String tipo,
        @NotNull(message = "Valor inicial é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valorInicial,
        BigDecimal quantidadeCotas,
        LocalDate dataInicio,
        @NotNull(message = "Data de vencimento é obrigatória") LocalDate dataVencimento,
        Boolean deduzirCarteira
) {}
