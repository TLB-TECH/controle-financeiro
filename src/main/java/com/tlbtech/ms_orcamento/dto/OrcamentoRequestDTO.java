package com.tlbtech.ms_orcamento.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrcamentoRequestDTO(

        @NotNull(message = "Centro de custo obrigatório")
        Long centroCustoId,

        @NotNull(message = "Mês obrigatório")
        @Min(value = 1, message = "Mês inválido")
        @Max(value = 12, message = "Mês inválido")
        Integer mes,

        @NotNull(message = "Ano obrigatório")
        @Min(value = 2000, message = "Ano Inválido")
        Integer ano,

        @NotNull(message = "Valor limite obrigatório")
        @Positive(message = "Valor limite deve ser positivo")
        BigDecimal valorLimite

) {}
