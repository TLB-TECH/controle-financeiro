package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.entity.Lancamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LancamentoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoLancamento tipo,
        StatusLancamento status,
        LocalDate dataLancamento,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        String observacao,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public static LancamentoResponseDTO fromEntity(Lancamento lancamento) {
        return new LancamentoResponseDTO(
                lancamento.getId(),
                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getTipo(),
                lancamento.getStatus(),
                lancamento.getDataLancamento(),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getObservacao(),
                lancamento.getAtivo(),
                lancamento.getCriadoEm(),
                lancamento.getAtualizadoEm()
        );
    }
}
