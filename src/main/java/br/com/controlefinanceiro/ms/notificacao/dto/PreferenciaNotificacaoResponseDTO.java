package br.com.controlefinanceiro.ms.notificacao.dto;

import br.com.controlefinanceiro.ms.notificacao.model.CanalNotificacao;
import br.com.controlefinanceiro.ms.notificacao.model.PreferenciaNotificacao;

public record PreferenciaNotificacaoResponseDTO(
        Long id,
        String usuarioId,
        CanalNotificacao canal,
        String telefone,
        Integer horaNotificacao,
        Boolean ativo
) {
    public static PreferenciaNotificacaoResponseDTO fromEntity(PreferenciaNotificacao entity) {
        return new PreferenciaNotificacaoResponseDTO(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getCanal(),
                entity.getTelefone(),
                entity.getHoraNotificacao(),
                entity.getAtivo()
        );
    }
}
