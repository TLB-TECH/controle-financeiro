package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.entity.Lancamento;
import br.com.controlefinanceiro.mslancamentos.enums.FormaPagamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoJuros;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LancamentoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valorOriginal,
        BigDecimal taxaJuros,
        TipoJuros tipoJuros,
        BigDecimal juros,
        BigDecimal desconto,
        BigDecimal valor,
        TipoLancamento tipo,
        FormaPagamento formaPagamento,
        Integer numeroParcelas,
        Integer parcelaNumero,
        String grupoParcelaId,
        Long cartaoCreditoId,
        StatusLancamento status,
        LocalDate dataLancamento,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        String observacao,
        Long centroCustoId,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public static LancamentoResponseDTO fromEntity(Lancamento l) {
        return new LancamentoResponseDTO(
                l.getId(),
                l.getDescricao(),
                l.getValorOriginal(),
                l.getTaxaJuros(),
                l.getTipoJuros(),
                l.getJuros(),
                l.getDesconto(),
                l.getValor(),
                l.getTipo(),
                l.getFormaPagamento(),
                l.getNumeroParcelas(),
                l.getParcelaNumero(),
                l.getGrupoParcelaId(),
                l.getCartaoCreditoId(),
                l.getStatus(),
                l.getDataLancamento(),
                l.getDataVencimento(),
                l.getDataPagamento(),
                l.getObservacao(),
                l.getCentroCustoId(),
                l.getAtivo(),
                l.getCriadoEm(),
                l.getAtualizadoEm()
        );
    }
}