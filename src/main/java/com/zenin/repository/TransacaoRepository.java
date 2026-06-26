package com.zenin.repository;

import com.zenin.model.Transacao;
import com.zenin.model.TipoTransacao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    @EntityGraph(attributePaths = {"carteira", "categoria", "transacaoRecorrente"})
    List<Transacao> findByUsuarioId(UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "categoria", "transacaoRecorrente"})
    Optional<Transacao> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    List<Transacao> findByUsuarioIdAndCarteiraId(UUID usuarioId, UUID carteiraId);

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    List<Transacao> findByUsuarioIdAndTipo(UUID usuarioId, TipoTransacao tipo);

    @EntityGraph(attributePaths = {"carteira", "categoria"})
    List<Transacao> findByUsuarioIdAndDataBetween(UUID usuarioId, LocalDate inicio, LocalDate fim);

    List<Transacao> findByUsuarioIdAndPago(UUID usuarioId, boolean pago);

    List<Transacao> findByTransacaoRecorrenteIdAndDataBetween(UUID transacaoRecorrenteId, LocalDate inicio, LocalDate fim);
}
