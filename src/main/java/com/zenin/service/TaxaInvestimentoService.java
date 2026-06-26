package com.zenin.service;

import com.zenin.dto.request.TaxaInvestimentoRequest;
import com.zenin.model.TaxaInvestimento;
import com.zenin.repository.TaxaInvestimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxaInvestimentoService {

    private final TaxaInvestimentoRepository taxaRepository;

    public List<TaxaInvestimento> listar() {
        return taxaRepository.findAll();
    }

    public List<TaxaInvestimento> listarPorTipo(String tipo) {
        return taxaRepository.findByTipo(tipo);
    }

    public TaxaInvestimento buscar(UUID id) {
        return taxaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Taxa de investimento não encontrada"));
    }

    public Optional<TaxaInvestimento> buscarUltimaPorTipo(String tipo) {
        return taxaRepository.findTopByTipoOrderByDataReferenciaDesc(tipo);
    }

    @Transactional
    public TaxaInvestimento criar(TaxaInvestimentoRequest request) {
        if (taxaRepository.findByTipoAndDataReferencia(request.tipo(), request.dataReferencia()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma taxa para esse tipo e data de referência");
        }

        TaxaInvestimento taxa = TaxaInvestimento.builder()
                .tipo(request.tipo())
                .nome(request.nome())
                .taxaAnual(request.taxaAnual())
                .taxaDiaria(request.taxaDiaria())
                .puVenda(request.puVenda())
                .puBase(request.puBase())
                .vencimento(request.vencimento())
                .dataReferencia(request.dataReferencia())
                .build();

        return taxaRepository.save(taxa);
    }

    @Transactional
    public TaxaInvestimento atualizar(UUID id, TaxaInvestimentoRequest request) {
        TaxaInvestimento taxa = buscar(id);

        taxa.setTipo(request.tipo());
        taxa.setNome(request.nome());
        taxa.setTaxaAnual(request.taxaAnual());
        taxa.setTaxaDiaria(request.taxaDiaria());
        taxa.setPuVenda(request.puVenda());
        taxa.setPuBase(request.puBase());
        taxa.setVencimento(request.vencimento());
        taxa.setDataReferencia(request.dataReferencia());

        return taxaRepository.save(taxa);
    }

    @Transactional
    public void deletar(UUID id) {
        TaxaInvestimento taxa = buscar(id);
        taxaRepository.delete(taxa);
    }
}
