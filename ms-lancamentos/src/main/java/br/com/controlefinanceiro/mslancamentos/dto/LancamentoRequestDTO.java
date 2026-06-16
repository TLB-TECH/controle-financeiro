package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequestDTO(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Tipo é obrigatório")
        TipoLancamento tipo,

        @NotNull(message = "Data é obrigatória")
        LocalDate dataLancamento,

        String observacao
) {}
