package com.zenin.dto.request;

import com.zenin.model.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransacaoRecorrenteRequest(
        UUID categoriaId,
        @NotNull(message = "Carteira é obrigatória") UUID carteiraId,
        @NotNull(message = "Tipo é obrigatório") TipoTransacao tipo,
        @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor,
        @NotBlank(message = "Nome é obrigatório") String nome,
        Integer diaVencimento
) {}
