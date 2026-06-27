package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.enums.FormaPagamento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoParceladoRequestDTO(
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor total é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valorTotal,

        @NotNull(message = "Número de parcelas é obrigatório")
        @Min(value = 1, message = "Mínimo 1 parcela")
        @Max(value = 60, message = "Máximo 60 parcelas")
        Integer numeroParcelas,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        // Obrigatório apenas quando formaPagamento = CARTAO_CREDITO
        Long cartaoCreditoId,

        Long centroCustoId,

        LocalDate dataInicio,

        String observacao
) {}