package br.com.controlefinanceiro.ms.notificacao.dto;

import br.com.controlefinanceiro.ms.notificacao.model.CanalNotificacao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PreferenciaNotificacaoRequestDTO(

        @NotNull(message = "Canal é obrigatório")
        CanalNotificacao canal,

        String telefone,

        @NotNull(message = "Hora de notificação é obrigatória")
        @Min(value = 0, message = "Hora minima é 0")
        @Max(value = 23, message = "Hora máxima é 23")
        Integer horaNotificacao
) {}
