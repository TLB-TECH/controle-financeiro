package br.com.controlefinanceiro.mslancamentos.entity;

import br.com.controlefinanceiro.mslancamentos.enums.FormaPagamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoJuros;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "valor_original", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorOriginal;

    @Column(name = "taxa_juros", precision = 5, scale = 2)
    private BigDecimal taxaJuros;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_juros", length = 10)
    private TipoJuros tipoJuros;

    @Column(precision = 15, scale = 2)
    private BigDecimal juros;

    @Column(precision = 15, scale = 2)
    private BigDecimal desconto;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoLancamento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusLancamento status;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 20)
    private FormaPagamento formaPagamento;

    @Column(name = "numero_parcelas")
    @Builder.Default
    private Integer numeroParcelas = 1;

    private String observacao;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.status = StatusLancamento.PENDENTE;
        this.ativo = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}