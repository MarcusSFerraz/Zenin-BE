package com.zenin.service;

import com.zenin.dto.request.AporteInvestimentoRequest;
import com.zenin.model.AporteInvestimento;
import com.zenin.model.Carteira;
import com.zenin.model.Investimento;
import com.zenin.model.User;
import com.zenin.repository.AporteInvestimentoRepository;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.InvestimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AporteInvestimentoService {

    private final AporteInvestimentoRepository aporteInvestimentoRepository;
    private final InvestimentoRepository investimentoRepository;
    private final CarteiraRepository carteiraRepository;

    public List<AporteInvestimento> listarPorUsuario(User usuario) {
        return aporteInvestimentoRepository.findByUsuarioId(usuario.getId());
    }

    public List<AporteInvestimento> listarPorInvestimento(UUID investimentoId, User usuario) {
        investimentoRepository.findByIdAndUsuarioId(investimentoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
        return aporteInvestimentoRepository.findByInvestimentoId(investimentoId);
    }

    @Transactional
    public AporteInvestimento criar(AporteInvestimentoRequest request, User usuario) {
        Investimento investimento = investimentoRepository
                .findByIdAndUsuarioId(request.investimentoId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));

        if (!investimento.isAtivo()) {
            throw new IllegalArgumentException("Não é possível aportar em um investimento encerrado");
        }

        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        if (carteira.getSaldoAtual().compareTo(request.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na carteira");
        }

        AporteInvestimento aporte = AporteInvestimento.builder()
                .investimento(investimento)
                .carteira(carteira)
                .usuario(usuario)
                .valor(request.valor())
                .build();

        investimento.setValorAtual(investimento.getValorAtual().add(request.valor()));
        carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(request.valor()));

        investimentoRepository.save(investimento);
        carteiraRepository.save(carteira);

        return aporteInvestimentoRepository.save(aporte);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        AporteInvestimento aporte = aporteInvestimentoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Aporte não encontrado"));

        Investimento investimento = aporte.getInvestimento();
        Carteira carteira = aporte.getCarteira();
        investimento.setValorAtual(investimento.getValorAtual().subtract(aporte.getValor()));
        carteira.setSaldoAtual(carteira.getSaldoAtual().add(aporte.getValor()));

        investimentoRepository.save(investimento);
        carteiraRepository.save(carteira);
        aporteInvestimentoRepository.delete(aporte);
    }
}
