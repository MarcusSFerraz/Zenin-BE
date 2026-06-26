package com.zenin.repository;

import com.zenin.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByUsuarioId(UUID usuarioId);
    Optional<Notificacao> findByIdAndUsuarioId(UUID id, UUID usuarioId);
    List<Notificacao> findByUsuarioIdAndLida(UUID usuarioId, boolean lida);
    long countByUsuarioIdAndLida(UUID usuarioId, boolean lida);
}
