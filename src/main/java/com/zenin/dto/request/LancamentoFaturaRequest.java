package com.zenin.dto.request;

import com.zenin.model.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LancamentoFaturaRequest(
        @NotBlank(message = "Descrição é obrigatória") String descricao,
        @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor,
        @NotNull(message = "Tipo é obrigatório") TipoLancamento tipo
) {}
