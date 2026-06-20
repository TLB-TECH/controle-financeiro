package br.com.controlefinanceiro.mslancamentos.dto;

public record CentroCustoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Boolean ativo
) {}
