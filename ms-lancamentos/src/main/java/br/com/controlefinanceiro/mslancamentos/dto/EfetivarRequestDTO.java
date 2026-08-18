package br.com.controlefinanceiro.mslancamentos.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EfetivarRequestDTO(
        @NotNull(message = "Conta é obrigatória")
        Long contaId,

        LocalDate dataPagamento
) {}
