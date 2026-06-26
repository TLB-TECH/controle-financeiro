package br.com.controlefinanceiro.ms.notificacao.scheduler;

import br.com.controlefinanceiro.ms.notificacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificacaoScheduler {

    private final NotificacaoService notificacaoService;

    @Scheduled(cron = "0 0 * * * *")
    public void executar() {
        log.info("Scheduler de notificações iniciado.");
        notificacaoService.processarNotificacoes();
        log.info("Scheduler de notificações finalizado.");
    }
}

@RestController
@RequestMapping("/interno/notificacao")
@RequiredArgsConstructor
class NotificacaoTestController {

    private final NotificacaoService notificacaoService;

    @PostMapping("/disparar")
    public ResponseEntity<String> disparar() {
        notificacaoService.processarNotificacoes();
        return ResponseEntity.ok("Notificações processadas!");
    }
}