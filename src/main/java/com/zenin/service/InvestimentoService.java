package com.zenin.service;

import com.zenin.dto.request.InvestimentoRequest;
import com.zenin.model.Carteira;
import com.zenin.model.Investimento;
import com.zenin.model.TaxaInvestimento;
import com.zenin.model.User;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.InvestimentoRepository;
import com.zenin.repository.TaxaInvestimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final CarteiraRepository carteiraRepository;
    private final TaxaInvestimentoRepository taxaRepository;

    public List<Investimento> listar(User usuario, Boolean ativo) {
        if (ativo != null) {
            return investimentoRepository.findByUsuarioIdAndAtivo(usuario.getId(), ativo);
        }
        return investimentoRepository.findByUsuarioId(usuario.getId());
    }

    public Investimento buscar(UUID id, User usuario) {
        return investimentoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
    }

    @Transactional
    public Investimento criar(InvestimentoRequest request, User usuario) {
        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        TaxaInvestimento taxa = null;
        if (request.taxaId() != null) {
            taxa = taxaRepository.findById(request.taxaId())
                    .orElseThrow(() -> new EntityNotFoundException("Taxa de investimento não encontrada"));
        }

        LocalDate dataInicio = request.dataInicio() != null ? request.dataInicio() : LocalDate.now();

        Investimento investimento = Investimento.builder()
                .usuario(usuario)
                .carteira(carteira)
                .taxa(taxa)
                .tipo(request.tipo())
                .valorInicial(request.valorInicial())
                .valorAtual(request.valorInicial())
                .quantidadeCotas(request.quantidadeCotas() != null ? request.quantidadeCotas() : BigDecimal.ZERO)
                .dataInicio(dataInicio)
                .dataVencimento(request.dataVencimento())
                .ativo(true)
                .build();

        carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(request.valorInicial()));
        carteiraRepository.save(carteira);

        return investimentoRepository.save(investimento);
    }

    @Transactional
    public Investimento atualizar(UUID id, InvestimentoRequest request, User usuario) {
        Investimento investimento = buscar(id, usuario);

        TaxaInvestimento taxa = null;
        if (request.taxaId() != null) {
            taxa = taxaRepository.findById(request.taxaId())
                    .orElseThrow(() -> new EntityNotFoundException("Taxa de investimento não encontrada"));
        }

        investimento.setTaxa(taxa);
        investimento.setTipo(request.tipo());
        investimento.setDataVencimento(request.dataVencimento());
        if (request.quantidadeCotas() != null) investimento.setQuantidadeCotas(request.quantidadeCotas());

        return investimentoRepository.save(investimento);
    }

    @Transactional
    public Investimento encerrar(UUID id, User usuario) {
        Investimento investimento = buscar(id, usuario);
        investimento.setAtivo(false);
        return investimentoRepository.save(investimento);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Investimento investimento = buscar(id, usuario);
        investimentoRepository.delete(investimento);
    }
}
