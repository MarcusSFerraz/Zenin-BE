package com.zenin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "carteiras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String nome;

    @Builder.Default
    private String cor = "#3B82F6";

    @Column(name = "saldo_inicial", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "saldo_atual", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @Column(name = "cartao_credito", nullable = false)
    @Builder.Default
    private boolean cartaoCredito = false;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        criadoEm = OffsetDateTime.now();
        atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }
}
