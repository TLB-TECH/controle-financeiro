package com.tlbtech.ms_contas.dto;

import com.tlbtech.ms_contas.model.Conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponseDTO(
        Long id,
        String nome,
        String banco,
        TipoContaResponseDTO tipoConta,
        BigDecimal saldo,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public static ContaResponseDTO fromEntity(Conta conta) {
        return new ContaResponseDTO(
                conta.getId(),
                conta.getNome(),
                conta.getBanco(),
                conta.getTipoConta() != null ? TipoContaResponseDTO.fromEntity(conta.getTipoConta()) : null,
                conta.getSaldo(),
                conta.getAtivo(),
                conta.getCriadoEm()
        );
    }
}
