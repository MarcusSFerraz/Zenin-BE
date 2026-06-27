package com.zenin.service;

import com.zenin.dto.request.FaturaCartaoRequest;
import com.zenin.model.Carteira;
import com.zenin.model.FaturaCartao;
import com.zenin.model.User;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.FaturaCartaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaturaCartaoService {

    private final FaturaCartaoRepository faturaRepository;
    private final CarteiraRepository carteiraRepository;

    public List<FaturaCartao> listar(User usuario, UUID carteiraId, Boolean pago) {
        if (carteiraId != null) {
            return faturaRepository.findByUsuarioIdAndCarteiraId(usuario.getId(), carteiraId);
        }
        if (pago != null) {
            return faturaRepository.findByUsuarioIdAndPago(usuario.getId(), pago);
        }
        return faturaRepository.findByUsuarioId(usuario.getId());
    }

    public FaturaCartao buscar(UUID id, User usuario) {
        return faturaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));
    }

    @Transactional
    public FaturaCartao criar(FaturaCartaoRequest request, User usuario) {
        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        if (!carteira.isCartaoCredito()) {
            throw new IllegalArgumentException("A carteira informada não é um cartão de crédito");
        }

        FaturaCartao fatura = FaturaCartao.builder()
                .usuario(usuario)
                .carteira(carteira)
                .mesReferencia(request.mesReferencia())
                .dataVencimento(request.dataVencimento())
                .valor(request.valor())
                .pago(false)
                .build();

        return faturaRepository.save(fatura);
    }

    @Transactional
    public FaturaCartao atualizar(UUID id, FaturaCartaoRequest request, User usuario) {
        FaturaCartao fatura = buscar(id, usuario);

        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        if (!carteira.isCartaoCredito()) {
            throw new IllegalArgumentException("A carteira informada não é um cartão de crédito");
        }

        fatura.setCarteira(carteira);
        fatura.setMesReferencia(request.mesReferencia());
        fatura.setDataVencimento(request.dataVencimento());
        fatura.setValor(request.valor());

        return faturaRepository.save(fatura);
    }

    @Transactional
    public FaturaCartao marcarComoPago(UUID id, UUID carteiraDebitoId, User usuario) {
        FaturaCartao fatura = buscar(id, usuario);

        if (fatura.isPago()) {
            throw new IllegalArgumentException("Fatura já está paga");
        }

        Carteira carteiraDebito = carteiraRepository.findByIdAndUsuarioId(carteiraDebitoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira de débito não encontrada"));

        if (carteiraDebito.getSaldoAtual().compareTo(fatura.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na carteira");
        }

        carteiraDebito.setSaldoAtual(carteiraDebito.getSaldoAtual().subtract(fatura.getValor()));
        carteiraRepository.save(carteiraDebito);

        fatura.setPago(true);
        return faturaRepository.save(fatura);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        FaturaCartao fatura = buscar(id, usuario);
        faturaRepository.delete(fatura);
    }
}
