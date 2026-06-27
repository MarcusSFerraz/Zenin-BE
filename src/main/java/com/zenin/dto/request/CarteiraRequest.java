package com.zenin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CarteiraRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        String cor,
        BigDecimal saldoInicial,
        boolean cartaoCredito,
        boolean cartaoDebito,
        boolean poupanca,
        @Min(value = 1, message = "Dia de vencimento mínimo é 1")
        @Max(value = 28, message = "Dia de vencimento máximo é 28")
        Integer diaVencimento
) {}
