package com.tlbtech.ms_orcamento.dto;

import com.tlbtech.ms_orcamento.model.MetaAplicacaoMensal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MetaMensalResponseDTO(
        Integer mes,
        Integer ano,
        BigDecimal valor,
        LocalDateTime atualizadoEm
) {
    public static MetaMensalResponseDTO fromEntity(MetaAplicacaoMensal m) {
        return new MetaMensalResponseDTO(m.getMes(), m.getAno(), m.getValor(), m.getAtualizadoEm());
    }

    public static MetaMensalResponseDTO vazio(Integer mes, Integer ano) {
        return new MetaMensalResponseDTO(mes, ano, BigDecimal.ZERO, null);
    }
}
