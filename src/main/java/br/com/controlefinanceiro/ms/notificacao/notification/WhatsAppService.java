package br.com.controlefinanceiro.ms.notificacao.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WhatsAppService {

    @Value("${evolution.api-url}")
    private String apiUrl;

    @Value("${evolution.api-key}")
    private String apiKey;

    @Value("${evolution.instance}")
    private String instance;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviar(String telefone, String mensagem) {
        try {
            String url = apiUrl + "/message/sendText/" + instance;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("number", telefone);
            body.put("text", mensagem);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);

            log.info("WhatsApp enviado para {}", telefone);
        } catch (Exception e) {
            log.error("Erro ao enviar WhatsApp para {}: {}", telefone, e.getMessage());
        }
    }
}