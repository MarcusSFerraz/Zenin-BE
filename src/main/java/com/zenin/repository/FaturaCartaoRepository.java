package com.zenin.repository;

import com.zenin.model.FaturaCartao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FaturaCartaoRepository extends JpaRepository<FaturaCartao, UUID> {

    @EntityGraph(attributePaths = {"carteira"})
    List<FaturaCartao> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira"})
    Optional<FaturaCartao> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira"})
    List<FaturaCartao> findByUsuarioIdAndCarteiraId(UUID usuarioId, UUID carteiraId);

    List<FaturaCartao> findByUsuarioIdAndPago(UUID usuarioId, boolean pago);
}
