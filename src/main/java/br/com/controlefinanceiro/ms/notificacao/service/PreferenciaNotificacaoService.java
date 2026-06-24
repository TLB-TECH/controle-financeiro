package br.com.controlefinanceiro.ms.notificacao.service;

import br.com.controlefinanceiro.ms.notificacao.dto.PreferenciaNotificacaoRequestDTO;
import br.com.controlefinanceiro.ms.notificacao.dto.PreferenciaNotificacaoResponseDTO;
import br.com.controlefinanceiro.ms.notificacao.model.PreferenciaNotificacao;

import br.com.controlefinanceiro.ms.notificacao.repository.PreferenciaNotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenciaNotificacaoService {

    private final PreferenciaNotificacaoRepository repositoy;

    public PreferenciaNotificacaoResponseDTO salvar(String usuarioId, PreferenciaNotificacaoRequestDTO dto) {
        repositoy.findByUsuarioIdAndAtivoTrue(usuarioId)
                .ifPresent(p -> {
                    p.setAtivo(false);
                    repositoy.save(p);
                });
        PreferenciaNotificacao preferencia = PreferenciaNotificacao.builder()
                .usuarioId(usuarioId)
                .canal(dto.canal())
                .telefone(dto.telefone())
                .horaNotificacao(dto.horaNotificacao())
                .build();
        return PreferenciaNotificacaoResponseDTO.fromEntity(repositoy.save(preferencia));
    }

    public PreferenciaNotificacaoResponseDTO buscar(String usuarioId) {
        PreferenciaNotificacao preferencia = repositoy.findByUsuarioIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new RuntimeException("Preferência não encontrada"));

        return PreferenciaNotificacaoResponseDTO.fromEntity(preferencia);
    }

    public void deletar(String usuarioId) {
        PreferenciaNotificacao preferencia = repositoy.findByUsuarioIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new RuntimeException("Preferência não encontrada"));

        preferencia.setAtivo(false);
        repositoy.save(preferencia);
    }
}
