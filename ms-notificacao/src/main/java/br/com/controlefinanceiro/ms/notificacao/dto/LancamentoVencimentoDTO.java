package br.com.controlefinanceiro.ms.notificacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoVencimentoDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate dataVencimento,
        String usuarioId
) {}
