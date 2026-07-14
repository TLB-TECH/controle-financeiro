package com.tlbtech.ms_contas.dto;

import com.tlbtech.ms_contas.model.CategoriaTipoConta;
import com.tlbtech.ms_contas.model.TipoConta;

import java.time.LocalDateTime;

public record TipoContaResponseDTO(
        Long id,
        String nome,
        CategoriaTipoConta categoria,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static TipoContaResponseDTO fromEntity(TipoConta tipoConta) {
        return new TipoContaResponseDTO(
                tipoConta.getId(),
                tipoConta.getNome(),
                tipoConta.getCategoria(),
                tipoConta.getAtivo(),
                tipoConta.getCriadoEm(),
                tipoConta.getAtualizadoEm()
        );
    }
}
