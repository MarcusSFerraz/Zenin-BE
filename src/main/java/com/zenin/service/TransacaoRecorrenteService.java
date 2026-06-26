package com.zenin.service;

import com.zenin.dto.request.TransacaoRecorrenteRequest;
import com.zenin.model.*;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.CategoriaRepository;
import com.zenin.repository.TransacaoRecorrenteRepository;
import com.zenin.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoRecorrenteService {

    private final TransacaoRecorrenteRepository transacaoRecorrenteRepository;
    private final CarteiraRepository carteiraRepository;
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRepository transacaoRepository;

    public List<TransacaoRecorrente> listar(User usuario) {
        return transacaoRecorrenteRepository.findByUsuarioId(usuario.getId());
    }

    public List<TransacaoRecorrente> listarAtivas(User usuario) {
        return transacaoRecorrenteRepository.findByUsuarioIdAndAtivo(usuario.getId(), true);
    }

    public TransacaoRecorrente buscar(UUID id, User usuario) {
        return transacaoRecorrenteRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transação recorrente não encontrada"));
    }

    @Transactional
    public TransacaoRecorrente criar(TransacaoRecorrenteRequest request, User usuario) {
        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        TransacaoRecorrente transacao = TransacaoRecorrente.builder()
                .usuario(usuario)
                .carteira(carteira)
                .categoria(categoria)
                .tipo(request.tipo())
                .valor(request.valor())
                .nome(request.nome())
                .diaVencimento(request.diaVencimento())
                .ativo(true)
                .build();

        return transacaoRecorrenteRepository.save(transacao);
    }

    @Transactional
    public TransacaoRecorrente atualizar(UUID id, TransacaoRecorrenteRequest request, User usuario) {
        TransacaoRecorrente transacao = buscar(id, usuario);

        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        transacao.setCarteira(carteira);
        transacao.setCategoria(categoria);
        transacao.setTipo(request.tipo());
        transacao.setValor(request.valor());
        transacao.setNome(request.nome());
        transacao.setDiaVencimento(request.diaVencimento());

        return transacaoRecorrenteRepository.save(transacao);
    }

    @Transactional
    public TransacaoRecorrente alternarAtivo(UUID id, User usuario) {
        TransacaoRecorrente transacao = buscar(id, usuario);
        transacao.setAtivo(!transacao.isAtivo());
        return transacaoRecorrenteRepository.save(transacao);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        TransacaoRecorrente transacao = buscar(id, usuario);
        transacaoRecorrenteRepository.delete(transacao);
    }

    @Transactional
    public List<Transacao> processar(User usuario) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        List<TransacaoRecorrente> ativas = transacaoRecorrenteRepository
                .findByUsuarioIdAndAtivo(usuario.getId(), true);

        List<Transacao> geradas = new ArrayList<>();

        for (TransacaoRecorrente recorrente : ativas) {
            boolean jaProcessada = !transacaoRepository
                    .findByTransacaoRecorrenteIdAndDataBetween(recorrente.getId(), inicioMes, fimMes)
                    .isEmpty();

            if (!jaProcessada) {
                int dia = recorrente.getDiaVencimento() != null
                        ? Math.min(recorrente.getDiaVencimento(), fimMes.getDayOfMonth())
                        : 1;

                Transacao transacao = Transacao.builder()
                        .usuario(usuario)
                        .carteira(recorrente.getCarteira())
                        .categoria(recorrente.getCategoria())
                        .transacaoRecorrente(recorrente)
                        .tipo(recorrente.getTipo())
                        .valor(recorrente.getValor())
                        .descricao("Recorrência: " + recorrente.getNome())
                        .data(inicioMes.withDayOfMonth(dia))
                        .dataVencimento(inicioMes.withDayOfMonth(dia))
                        .pago(false)
                        .build();

                geradas.add(transacaoRepository.save(transacao));
            }
        }

        return geradas;
    }
}
