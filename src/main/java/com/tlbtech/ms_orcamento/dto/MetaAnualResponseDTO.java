package com.tlbtech.ms_orcamento.dto;

import com.tlbtech.ms_orcamento.model.MetaAplicacaoAnual;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MetaAnualResponseDTO(
        Integer ano,
        BigDecimal valor,
        LocalDateTime atualizadoEm
) {
    public static MetaAnualResponseDTO fromEntity(MetaAplicacaoAnual m) {
        return new MetaAnualResponseDTO(m.getAno(), m.getValor(), m.getAtualizadoEm());
    }

    public static MetaAnualResponseDTO vazio(Integer ano) {
        return new MetaAnualResponseDTO(ano, BigDecimal.ZERO, null);
    }
}
