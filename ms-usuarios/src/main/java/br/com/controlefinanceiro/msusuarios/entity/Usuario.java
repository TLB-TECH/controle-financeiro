package br.com.controlefinanceiro.msusuarios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "reset_token", length = 255)
    private String resetToken;

    @Column(name = "reset_token_expiracao")
    private LocalDateTime resetTokenExpiracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_assinatura", nullable = false, length = 20)
    @Builder.Default
    private StatusAssinatura statusAssinatura = StatusAssinatura.TRIAL;

    @Column(name = "trial_fim", nullable = false)
    private LocalDateTime trialFim;

    @Column(name = "mp_preapproval_id", length = 100)
    private String mpPreapprovalId;

    @Column(name = "assinatura_atualizada_em")
    private LocalDateTime assinaturaAtualizadaEm;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;
}
