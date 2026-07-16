package com.tlbtech.ms_orcamento.dto;

import com.tlbtech.ms_orcamento.model.Orcamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoResponseDTO(
        Long id,
        Long centroCustoId,
        Integer mes,
        Integer ano,
        BigDecimal valorLimite,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public static OrcamentoResponseDTO fromEntity(Orcamento o) {
        return new OrcamentoResponseDTO(
                o.getId(),
                o.getCentroCustoId(),
                o.getMes(),
                o.getAno(),
                o.getValorLimite(),
                o.getAtivo(),
                o.getCriadoEm()
        );
    }
}