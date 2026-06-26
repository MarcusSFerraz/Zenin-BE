package com.zenin.repository;

import com.zenin.model.TaxaInvestimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxaInvestimentoRepository extends JpaRepository<TaxaInvestimento, UUID> {
    List<TaxaInvestimento> findByTipo(String tipo);
    Optional<TaxaInvestimento> findByTipoAndDataReferencia(String tipo, LocalDate dataReferencia);
    Optional<TaxaInvestimento> findTopByTipoOrderByDataReferenciaDesc(String tipo);
}
