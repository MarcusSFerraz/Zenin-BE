package com.zenin.repository;

import com.zenin.model.LancamentoFatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LancamentoFaturaRepository extends JpaRepository<LancamentoFatura, UUID> {

    List<LancamentoFatura> findByFaturaIdAndUsuarioId(UUID faturaId, UUID usuarioId);

    Optional<LancamentoFatura> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
