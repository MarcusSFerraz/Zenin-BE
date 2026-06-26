package com.zenin.dto.response;

import com.zenin.model.Carteira;

import java.util.UUID;

public record CarteiraResumo(UUID id, String nome, String cor) {
    public static CarteiraResumo from(Carteira c) {
        if (c == null) return null;
        return new CarteiraResumo(c.getId(), c.getNome(), c.getCor());
    }
}
