package com.zenin.repository;

import com.zenin.model.Meta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetaRepository extends JpaRepository<Meta, UUID> {

    @EntityGraph(attributePaths = {"categoria"})
    List<Meta> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"categoria"})
    Optional<Meta> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
