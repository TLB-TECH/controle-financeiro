package br.com.controlefinanceiro.msusuarios.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url:http://localhost:4200}")
    private String appUrl;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void enviarEmailRecuperacao(String destinatario,String token) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(destinatario);
            msg.setSubject("Recuperação de Senha - Controle Financeiro");
            msg.setText(
                    "Olá!\n\n" +
                    "Recebemos uma solicitação para redefinir sua senha.\n\n" +
                    "Acesse o link abaixo (válido por 1 hora):\n" +
                    appUrl + "/redefinir-senha?token=" + token + "\n\n" +
                    "Se não foi você, ignore este e-mail.\n\n" +
                    "Equipe TLB TECH"
            );
            mailSender.send(msg);
            log.info("E-mail de recuperação enviado para: {}", destinatario);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para: {}", destinatario, e);
            throw new RuntimeException("Erro ao enviar e-mail de recuperação");
        }
    }
}
