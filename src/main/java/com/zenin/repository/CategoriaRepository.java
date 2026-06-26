package com.zenin.repository;

import com.zenin.model.Categoria;
import com.zenin.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findByUsuarioId(UUID usuarioId);
    List<Categoria> findByUsuarioIdAndTipo(UUID usuarioId, TipoTransacao tipo);
    Optional<Categoria> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
