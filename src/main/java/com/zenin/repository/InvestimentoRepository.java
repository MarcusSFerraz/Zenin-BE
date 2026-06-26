package com.zenin.repository;

import com.zenin.model.Investimento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestimentoRepository extends JpaRepository<Investimento, UUID> {

    @EntityGraph(attributePaths = {"carteira", "taxa"})
    List<Investimento> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "taxa"})
    List<Investimento> findByUsuarioIdAndAtivo(UUID usuarioId, boolean ativo);

    @EntityGraph(attributePaths = {"carteira", "taxa"})
    Optional<Investimento> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
