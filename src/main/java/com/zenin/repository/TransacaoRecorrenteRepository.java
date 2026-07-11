package com.zenin.repository;

import com.zenin.model.TransacaoRecorrente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoRecorrenteRepository extends JpaRepository<TransacaoRecorrente, UUID> {

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    List<TransacaoRecorrente> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    List<TransacaoRecorrente> findByUsuarioIdAndAtivo(UUID usuarioId, boolean ativo);

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    Optional<TransacaoRecorrente> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "categoria", "usuario"})
    List<TransacaoRecorrente> findAllByAtivo(boolean ativo);
}
