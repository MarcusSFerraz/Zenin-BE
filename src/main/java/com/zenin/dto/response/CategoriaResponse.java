package com.zenin.dto.response;

import com.zenin.model.Categoria;
import com.zenin.model.TipoTransacao;

import java.util.UUID;

public record CategoriaResponse(
        UUID id, String nome, TipoTransacao tipo, String cor, String icone
) {
    public static CategoriaResponse from(Categoria c) {
        if (c == null) return null;
        return new CategoriaResponse(c.getId(), c.getNome(), c.getTipo(), c.getCor(), c.getIcone());
    }
}
