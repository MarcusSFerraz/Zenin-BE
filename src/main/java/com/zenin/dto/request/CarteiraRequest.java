package com.zenin.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CarteiraRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        String cor,
        BigDecimal saldoInicial,
        boolean cartaoCredito
) {}
