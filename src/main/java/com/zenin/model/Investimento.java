package com.zenin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "investimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxa_id")
    private TaxaInvestimento taxa;

    @Column(nullable = false)
    @Builder.Default
    private String tipo = "tesouro_selic";

    @Column(name = "valor_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorInicial;

    @Column(name = "valor_atual", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAtual;

    @Column(name = "quantidade_cotas", precision = 20, scale = 8)
    @Builder.Default
    private BigDecimal quantidadeCotas = BigDecimal.ZERO;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        if (dataInicio == null) dataInicio = LocalDate.now();
        criadoEm = OffsetDateTime.now();
        atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }
}
