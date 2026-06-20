package br.com.controlefinanceiro.ms.fluxo.caixa.dto;

import java.math.BigDecimal;

public record FluxoCaixaResponseDTO(
        BigDecimal totalEntradas,
        BigDecimal totalSaidas,
        BigDecimal saldo
) {}
