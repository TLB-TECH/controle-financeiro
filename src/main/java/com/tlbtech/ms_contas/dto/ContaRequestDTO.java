package com.tlbtech.ms_contas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Banco é obrigatório")
        String banco,

        @NotNull(message = "Tipo é obrigatório")
        Long tipoContaId,

        @NotNull(message = "Saldo é obrigatório")
        BigDecimal saldo

) {}
