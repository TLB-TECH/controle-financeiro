package br.com.controlefinanceiro.mslancamentos.dto;

import java.time.LocalDate;

public record EfetivarRequestDTO(
        LocalDate dataPagamento
) {}
