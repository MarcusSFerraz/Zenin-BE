package com.zenin.repository;

import com.zenin.model.AporteMeta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AporteMetaRepository extends JpaRepository<AporteMeta, UUID> {

    @EntityGraph(attributePaths = {"carteira", "meta"})
    List<AporteMeta> findByMetaId(UUID metaId);

    @EntityGraph(attributePaths = {"carteira", "meta"})
    List<AporteMeta> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "meta"})
    Optional<AporteMeta> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
