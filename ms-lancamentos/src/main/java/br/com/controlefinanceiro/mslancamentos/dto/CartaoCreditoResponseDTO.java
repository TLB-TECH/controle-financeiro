package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.entity.CartaoCredito;

import java.math.BigDecimal;

public record CartaoCreditoResponseDTO(
        Long id,
        String nome,
        BigDecimal limite,
        Boolean ativo
) {
    public static CartaoCreditoResponseDTO fromEntity(CartaoCredito c) {
        return new CartaoCreditoResponseDTO(
                c.getId(),
                c.getNome(),
                c.getLimite(),
                c.getAtivo()
        );
    }
}