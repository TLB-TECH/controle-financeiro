package com.tlbtech.ms_orcamento.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MetaMensalItemDTO(

        @NotNull(message = "Mês obrigatório")
        @Min(value = 1, message = "Mês inválido")
        @Max(value = 12, message = "Mês inválido")
        Integer mes,

        @NotNull(message = "Valor obrigatório")
        @PositiveOrZero(message = "Valor não pode ser negativo")
        BigDecimal valor

) {}
