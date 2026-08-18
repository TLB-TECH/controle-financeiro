package br.com.controlefinanceiro.ms.notificacao.service;

import br.com.controlefinanceiro.ms.notificacao.client.LancamentoClient;
import br.com.controlefinanceiro.ms.notificacao.dto.LancamentoVencimentoDTO;
import br.com.controlefinanceiro.ms.notificacao.model.CanalNotificacao;
import br.com.controlefinanceiro.ms.notificacao.model.PreferenciaNotificacao;
import br.com.controlefinanceiro.ms.notificacao.notification.EmailService;
import br.com.controlefinanceiro.ms.notificacao.notification.WhatsAppService;
import br.com.controlefinanceiro.ms.notificacao.repository.PreferenciaNotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final LancamentoClient lancamentoClient;
    private final PreferenciaNotificacaoRepository preferenciaRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    @Value("${internal.secret}")
    private String internalSecret;

    public void processarNotificacoes() {
        int horaAtual = LocalTime.now().getHour();
        log.info("Processando notificações para hora: {}", horaAtual);

        List<PreferenciaNotificacao> preferencias = preferenciaRepository.findAllByHoraNotificacaoAndAtivoTrue(horaAtual);

        if (preferencias.isEmpty()) {
            log.info("Nenhum usuário para notificar neste hora.");
            return;
        }

        List<LancamentoVencimentoDTO> lancamentos =
                lancamentoClient.buscarLancamentosVencendoHoje(internalSecret);

        for (PreferenciaNotificacao preferencia : preferencias) {
            List<LancamentoVencimentoDTO> lancamentosDoUsuario = lancamentos.stream()
                    .filter(l -> l.usuarioId().equals(preferencia.getUsuarioId()))
                    .toList();

            if (lancamentosDoUsuario.isEmpty()) continue;

            String mensagem = montarMensagem(lancamentosDoUsuario);

            if (preferencia.getCanal() == CanalNotificacao.EMAIL) {
                emailService.enviar(
                        preferencia.getUsuarioId(),
                        "⚠️ Lançamentos vencendo hoje!",
                        mensagem
                );
            } else if (preferencia.getCanal() == CanalNotificacao.WHATSAPP) {
                whatsAppService.enviar(preferencia.getTelefone(), mensagem);
            }
        }
    }

    private  String montarMensagem(List<LancamentoVencimentoDTO> lancamentos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá! Você tem lançamentos vencendo hoje:\n\n");
        for (LancamentoVencimentoDTO l : lancamentos) {
            sb.append("• ").append(l.descricao())
                    .append(" — R$ ").append(l.valor())
                    .append("\n");
        }
        sb.append("\nAcesse o sistema para mais detalhes.");
        return sb.toString();
    }
}