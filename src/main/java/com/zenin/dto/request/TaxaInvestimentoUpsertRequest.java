package com.zenin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxaInvestimentoUpsertRequest(
        @JsonProperty("name") String name,
        @JsonProperty("rate_type") String rateType,
        @JsonProperty("pu_base") BigDecimal puBase,
        @JsonProperty("pu_venda") BigDecimal puVenda,
        @JsonProperty("reference_date") LocalDate referenceDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @JsonProperty("vencimento") LocalDate vencimento,
        @JsonProperty("annual_rate") BigDecimal annualRate,
        @JsonProperty("daily_rate") BigDecimal dailyRate
) {}
