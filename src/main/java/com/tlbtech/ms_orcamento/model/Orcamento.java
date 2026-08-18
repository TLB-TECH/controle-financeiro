package com.tlbtech.ms_orcamento.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alvo", nullable = false, length = 20)
    private TipoAlvoOrcamento tipoAlvo;

    @Column(name = "centro_custo_id")
    private Long centroCustoId;

    @Column(name = "cartao_credito_id")
    private Long cartaoCreditoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoOrcamento tipo;

    @Column
    private Integer mes;

    @Column
    private Integer ano;

    @Column(name = "valor_limite", nullable = false)
    private BigDecimal valorLimite;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.ativo =true;
        this.criadoEm = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoAlvoOrcamento getTipoAlvo() { return tipoAlvo; }
    public void setTipoAlvo(TipoAlvoOrcamento tipoAlvo) { this.tipoAlvo = tipoAlvo; }

    public Long getCentroCustoId() { return centroCustoId; }
    public void setCentroCustoId(Long centroCustoId) { this.centroCustoId = centroCustoId; }

    public Long getCartaoCreditoId() { return cartaoCreditoId; }
    public void setCartaoCreditoId(Long cartaoCreditoId) { this.cartaoCreditoId = cartaoCreditoId; }

    public TipoOrcamento getTipo() { return tipo; }
    public void setTipo(TipoOrcamento tipo) { this.tipo = tipo; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public BigDecimal getValorLimite() { return valorLimite; }
    public void setValorLimite(BigDecimal valorLimite) { this.valorLimite = valorLimite; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
