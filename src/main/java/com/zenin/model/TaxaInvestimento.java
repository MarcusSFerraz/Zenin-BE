package com.zenin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "taxas_investimento",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tipo", "data_referencia"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxaInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String tipo;

    private String nome;

    @Column(name = "taxa_anual")
    private BigDecimal taxaAnual;

    @Column(name = "taxa_diaria", nullable = false)
    private BigDecimal taxaDiaria;

    @Column(name = "pu_venda")
    private BigDecimal puVenda;

    @Column(name = "pu_base")
    private BigDecimal puBase;

    private LocalDate vencimento;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm;

    @PrePersist
    void prePersist() {
        criadoEm = OffsetDateTime.now();
    }
}
