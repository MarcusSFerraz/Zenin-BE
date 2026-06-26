package com.zenin.repository;

import com.zenin.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarteiraRepository extends JpaRepository<Carteira, UUID> {
    List<Carteira> findByUsuarioId(UUID usuarioId);
    Optional<Carteira> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
