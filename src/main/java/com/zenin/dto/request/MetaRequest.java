package com.zenin.dto.request;

import com.zenin.model.TipoPeriodo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MetaRequest(
        UUID categoriaId,
        UUID investimentoId,
        UUID carteiraId,
        BigDecimal valorInicial,
        Boolean deduzirCarteira,
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotNull(message = "Valor alvo é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valorAlvo,
        @NotNull(message = "Período é obrigatório") TipoPeriodo periodo,
        @NotNull(message = "Data de início é obrigatória") LocalDate dataInicio,
        @NotNull(message = "Data de fim é obrigatória") LocalDate dataFim,
        String cor,
        String icone
) {}
