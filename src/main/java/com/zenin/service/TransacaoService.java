package com.zenin.service;

import com.zenin.dto.request.TransacaoRequest;
import com.zenin.model.*;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.CategoriaRepository;
import com.zenin.repository.TransacaoRecorrenteRepository;
import com.zenin.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CarteiraRepository carteiraRepository;
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRecorrenteRepository transacaoRecorrenteRepository;

    public List<Transacao> listar(User usuario, UUID carteiraId, LocalDate inicio, LocalDate fim) {
        if (carteiraId != null) {
            return transacaoRepository.findByUsuarioIdAndCarteiraId(usuario.getId(), carteiraId);
        }
        if (inicio != null && fim != null) {
            return transacaoRepository.findByUsuarioIdAndDataBetween(usuario.getId(), inicio, fim);
        }
        return transacaoRepository.findByUsuarioId(usuario.getId());
    }

    public Transacao buscar(UUID id, User usuario) {
        return transacaoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
    }

    @Transactional
    public Transacao criar(TransacaoRequest request, User usuario) {
        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        TransacaoRecorrente transacaoRecorrente = null;
        if (request.transacaoRecorrenteId() != null) {
            transacaoRecorrente = transacaoRecorrenteRepository
                    .findByIdAndUsuarioId(request.transacaoRecorrenteId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Transação recorrente não encontrada"));
        }

        Transacao transacao = Transacao.builder()
                .usuario(usuario)
                .carteira(carteira)
                .categoria(categoria)
                .transacaoRecorrente(transacaoRecorrente)
                .tipo(request.tipo())
                .valor(request.valor())
                .descricao(request.descricao())
                .data(request.data() != null ? request.data() : LocalDate.now())
                .dataVencimento(request.dataVencimento())
                .pago(request.pago())
                .pagoEm(request.pago() ? OffsetDateTime.now() : null)
                .build();

        if (request.pago()) {
            atualizarSaldoCarteira(carteira, request.tipo(), request.valor());
            carteiraRepository.save(carteira);
        }

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao atualizar(UUID id, TransacaoRequest request, User usuario) {
        Transacao transacao = buscar(id, usuario);

        Carteira novaCarteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        TransacaoRecorrente transacaoRecorrente = null;
        if (request.transacaoRecorrenteId() != null) {
            transacaoRecorrente = transacaoRecorrenteRepository
                    .findByIdAndUsuarioId(request.transacaoRecorrenteId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Transação recorrente não encontrada"));
        }

        // Reverte impacto da transação anterior se estava paga
        if (transacao.isPago()) {
            reverterSaldoCarteira(transacao.getCarteira(), transacao.getTipo(), transacao.getValor());
            carteiraRepository.save(transacao.getCarteira());
        }

        transacao.setCarteira(novaCarteira);
        transacao.setCategoria(categoria);
        transacao.setTransacaoRecorrente(transacaoRecorrente);
        transacao.setTipo(request.tipo());
        transacao.setValor(request.valor());
        transacao.setDescricao(request.descricao());
        if (request.data() != null) transacao.setData(request.data());
        transacao.setDataVencimento(request.dataVencimento());
        transacao.setPago(request.pago());
        transacao.setPagoEm(request.pago() ? OffsetDateTime.now() : null);

        if (request.pago()) {
            atualizarSaldoCarteira(novaCarteira, request.tipo(), request.valor());
            carteiraRepository.save(novaCarteira);
        }

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao marcarComoPago(UUID id, User usuario) {
        Transacao transacao = buscar(id, usuario);

        if (transacao.isPago()) {
            throw new IllegalArgumentException("Transação já está marcada como paga");
        }

        transacao.setPago(true);
        transacao.setPagoEm(OffsetDateTime.now());

        atualizarSaldoCarteira(transacao.getCarteira(), transacao.getTipo(), transacao.getValor());
        carteiraRepository.save(transacao.getCarteira());

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Transacao transacao = buscar(id, usuario);

        if (transacao.isPago()) {
            reverterSaldoCarteira(transacao.getCarteira(), transacao.getTipo(), transacao.getValor());
            carteiraRepository.save(transacao.getCarteira());
        }

        transacaoRepository.delete(transacao);
    }

    private void atualizarSaldoCarteira(Carteira carteira, TipoTransacao tipo, BigDecimal valor) {
        if (tipo == TipoTransacao.RECEITA) {
            carteira.setSaldoAtual(carteira.getSaldoAtual().add(valor));
        } else {
            carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(valor));
        }
    }

    private void reverterSaldoCarteira(Carteira carteira, TipoTransacao tipo, BigDecimal valor) {
        if (tipo == TipoTransacao.RECEITA) {
            carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(valor));
        } else {
            carteira.setSaldoAtual(carteira.getSaldoAtual().add(valor));
        }
    }
}
