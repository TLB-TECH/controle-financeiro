package br.com.controlefinanceiro.bff.financeiro.dto;

import java.math.BigDecimal;

public record FluxoCaixaResponseDTO(
        BigDecimal totalEntradas,
        BigDecimal totalSaidas,
        BigDecimal saldo
) {}
