package com.tlbtech.ms_orcamento.dto;

import com.tlbtech.ms_orcamento.model.TipoAlvoOrcamento;
import com.tlbtech.ms_orcamento.model.TipoOrcamento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrcamentoRequestDTO(

        @NotNull(message = "Alvo do orçamento obrigatório")
        TipoAlvoOrcamento tipoAlvo,

        Long centroCustoId,

        Long cartaoCreditoId,

        @NotNull(message = "Tipo de orçamento obrigatório")
        TipoOrcamento tipo,

        @Min(value = 1, message = "Mês inválido")
        @Max(value = 12, message = "Mês inválido")
        Integer mes,

        @Min(value = 2000, message = "Ano Inválido")
        Integer ano,

        @NotNull(message = "Valor limite obrigatório")
        @Positive(message = "Valor limite deve ser positivo")
        BigDecimal valorLimite

) {}
