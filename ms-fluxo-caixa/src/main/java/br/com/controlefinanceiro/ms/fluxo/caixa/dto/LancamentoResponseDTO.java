package br.com.controlefinanceiro.ms.fluxo.caixa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        String tipo,
        String status,
        LocalDate dataLancamento,
        LocalDate dataPagamento
) {}
