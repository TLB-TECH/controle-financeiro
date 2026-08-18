package br.com.controlefinanceiro.bff.financeiro.dto;

import java.math.BigDecimal;

public record LancamentoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        String tipo,
        String status,
        String formaPagamento,
        Long centroCustId
) {}
