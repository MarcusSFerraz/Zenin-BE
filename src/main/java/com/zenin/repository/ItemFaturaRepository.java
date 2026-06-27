package com.zenin.repository;

import com.zenin.model.ItemFatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemFaturaRepository extends JpaRepository<ItemFatura, UUID> {

    List<ItemFatura> findByFaturaIdAndUsuarioId(UUID faturaId, UUID usuarioId);

    Optional<ItemFatura> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
