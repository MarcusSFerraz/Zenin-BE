package com.zenin.service;

import com.zenin.dto.request.LancamentoFaturaRequest;
import com.zenin.model.FaturaCartao;
import com.zenin.model.LancamentoFatura;
import com.zenin.model.User;
import com.zenin.repository.FaturaCartaoRepository;
import com.zenin.repository.LancamentoFaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LancamentoFaturaService {

    private final LancamentoFaturaRepository lancamentoRepository;
    private final FaturaCartaoRepository faturaRepository;

    public List<LancamentoFatura> listar(UUID faturaId, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));
        return lancamentoRepository.findByFaturaIdAndUsuarioId(faturaId, usuario.getId());
    }

    @Transactional
    public LancamentoFatura criar(UUID faturaId, LancamentoFaturaRequest request, User usuario) {
        FaturaCartao fatura = faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        LancamentoFatura lancamento = LancamentoFatura.builder()
                .fatura(fatura)
                .usuario(usuario)
                .descricao(request.descricao())
                .valor(request.valor())
                .tipo(request.tipo())
                .build();

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public LancamentoFatura atualizar(UUID faturaId, UUID lancamentoId, LancamentoFaturaRequest request, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        LancamentoFatura lancamento = lancamentoRepository.findByIdAndUsuarioId(lancamentoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));

        lancamento.setDescricao(request.descricao());
        lancamento.setValor(request.valor());
        lancamento.setTipo(request.tipo());

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void deletar(UUID faturaId, UUID lancamentoId, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        LancamentoFatura lancamento = lancamentoRepository.findByIdAndUsuarioId(lancamentoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));

        lancamentoRepository.delete(lancamento);
    }
}
