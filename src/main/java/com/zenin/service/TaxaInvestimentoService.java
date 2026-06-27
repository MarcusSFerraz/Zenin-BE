package com.zenin.service;

import com.zenin.dto.request.TaxaInvestimentoRequest;
import com.zenin.dto.request.TaxaInvestimentoUpsertRequest;
import com.zenin.model.TaxaInvestimento;
import com.zenin.repository.TaxaInvestimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<TaxaInvestimento> buscarPorReferencia(String referencia) {
        return taxaRepository.findByReferencia(referencia);
    }

    @Transactional
    public List<TaxaInvestimento> upsertBulk(List<TaxaInvestimentoUpsertRequest> requests) {
        List<TaxaInvestimento> resultado = new ArrayList<>();
        for (TaxaInvestimentoUpsertRequest req : requests) {
            Optional<TaxaInvestimento> existente = taxaRepository.findFirstByReferencia(req.rateType());
            TaxaInvestimento taxa;
            if (existente.isPresent()) {
                taxa = existente.get();
            } else {
                String tipo = req.rateType().contains("_")
                        ? req.rateType().substring(0, req.rateType().lastIndexOf('_'))
                        : req.rateType();
                taxa = TaxaInvestimento.builder()
                        .tipo(tipo)
                        .referencia(req.rateType())
                        .build();
            }
            taxa.setNome(req.name());
            taxa.setTaxaAnual(req.annualRate());
            taxa.setTaxaDiaria(req.dailyRate());
            taxa.setPuBase(req.puBase());
            taxa.setPuVenda(req.puVenda());
            taxa.setVencimento(req.vencimento());
            taxa.setDataReferencia(req.referenceDate());
            resultado.add(taxaRepository.save(taxa));
        }
        return resultado;
    }

    @Transactional
    public TaxaInvestimento criar(TaxaInvestimentoRequest request) {
        if (taxaRepository.findByTipoAndDataReferencia(request.tipo(), request.dataReferencia()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma taxa para esse tipo e data de referência");
        }

        TaxaInvestimento taxa = TaxaInvestimento.builder()
                .tipo(request.tipo())
                .nome(request.nome())
                .referencia(request.referencia())
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
        taxa.setReferencia(request.referencia());
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
