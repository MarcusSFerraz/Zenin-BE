package com.zenin.repository;

import com.zenin.model.AporteMeta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("SELECT am.meta.id, COALESCE(SUM(am.valor), 0) FROM AporteMeta am WHERE am.meta.id IN :metaIds GROUP BY am.meta.id")
    List<Object[]> sumByMetaIds(@Param("metaIds") List<UUID> metaIds);
}
