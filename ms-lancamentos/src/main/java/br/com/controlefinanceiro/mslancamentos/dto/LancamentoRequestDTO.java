package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.enums.FormaPagamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoJuros;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequestDTO(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor origina é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valorOriginal,

        @DecimalMin(value = "0.00", message = "Taxa de juros não pode ser negativa")
        BigDecimal taxaJuros,

        TipoJuros tipoJuros,

        @DecimalMin(value = "0.00", message = "Juros não pode ser negativo")
        BigDecimal juros,

        @DecimalMin(value = "0.00", message = "Desconto não pode ser negativo")
        BigDecimal desconto,

        @NotNull(message = "Tipo é obrigatório")
        TipoLancamento tipo,

        FormaPagamento formaPagamento,

        @Min(value = 1, message = "Número de parcelas deve ser pelo menos 1")
        Integer numeroParcelas,

        LocalDate dataLancamento,

        LocalDate dataVencimento,

        String observacao
) {}
