package br.com.controlefinanceiro.mslancamentos.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CartaoCreditoRequestDTO(
        @NotBlank(message = "Nome do banco é obrigatório")
        String nome,

        BigDecimal limite
) {}