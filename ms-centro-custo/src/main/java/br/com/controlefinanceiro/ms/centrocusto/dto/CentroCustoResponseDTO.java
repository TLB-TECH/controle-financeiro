package br.com.controlefinanceiro.ms.centrocusto.dto;

import br.com.controlefinanceiro.ms.centrocusto.model.CentroCusto;

import java.time.LocalDateTime;

public record CentroCustoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static CentroCustoResponseDTO fromEntity(CentroCusto centroCusto) {
        return new CentroCustoResponseDTO(
                centroCusto.getId(),
                centroCusto.getNome(),
                centroCusto.getDescricao(),
                centroCusto.getAtivo(),
                centroCusto.getCriadoEm(),
                centroCusto.getAtualizadoEm()
        );
    }
}
