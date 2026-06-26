package com.zenin.repository;

import com.zenin.model.AporteInvestimento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AporteInvestimentoRepository extends JpaRepository<AporteInvestimento, UUID> {

    @EntityGraph(attributePaths = {"carteira", "investimento"})
    List<AporteInvestimento> findByInvestimentoId(UUID investimentoId);

    @EntityGraph(attributePaths = {"carteira", "investimento"})
    List<AporteInvestimento> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "investimento"})
    Optional<AporteInvestimento> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
