package com.tlbtech.ms_orcamento.dto;

import com.tlbtech.ms_orcamento.model.Orcamento;
import com.tlbtech.ms_orcamento.model.TipoAlvoOrcamento;
import com.tlbtech.ms_orcamento.model.TipoOrcamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoResponseDTO(
        Long id,
        TipoAlvoOrcamento tipoAlvo,
        Long centroCustoId,
        Long cartaoCreditoId,
        TipoOrcamento tipo,
        Integer mes,
        Integer ano,
        BigDecimal valorLimite,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public static OrcamentoResponseDTO fromEntity(Orcamento o) {
        return new OrcamentoResponseDTO(
                o.getId(),
                o.getTipoAlvo(),
                o.getCentroCustoId(),
                o.getCartaoCreditoId(),
                o.getTipo(),
                o.getMes(),
                o.getAno(),
                o.getValorLimite(),
                o.getAtivo(),
                o.getCriadoEm()
        );
    }
}
