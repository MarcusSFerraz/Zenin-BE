package com.zenin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PerfilUpdateRequest(
        String nome,
        String preferenciaTema,
        @Min(value = 1, message = "Dia de pagamento deve ser entre 1 e 31")
        @Max(value = 31, message = "Dia de pagamento deve ser entre 1 e 31")
        Integer diaPagamento,
        String numeroWhatsapp
) {}
