package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;

/**
 * Recebe as notificacoes de assinatura do Mercado Pago. Publico (sem JWT) por natureza -
 * a autenticidade da chamada e garantida validando a assinatura HMAC do header x-signature,
 * nao por token de usuario. Ver docs.mercadopago.com/webhooks para o algoritmo do manifest.
 */
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookMercadoPagoController {

    private final AssinaturaService assinaturaService;

    @Value("${mercadopago.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/mercadopago")
    public ResponseEntity<Void> receber(
            @RequestHeader(value = "x-signature", required = false) String xSignature,
            @RequestHeader(value = "x-request-id", required = false) String xRequestId,
            @RequestParam(value = "data.id", required = false) String dataIdQuery,
            @RequestBody(required = false) Map<String, Object> body) {

        String dataId = dataIdQuery != null ? dataIdQuery : extrairDataIdDoCorpo(body);

        if (dataId == null || !assinaturaValida(xSignature, xRequestId, dataId)) {
            log.warn("Webhook do Mercado Pago recusado: assinatura invalida ou data.id ausente");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        assinaturaService.processarWebhook(dataId);
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("unchecked")
    private String extrairDataIdDoCorpo(Map<String, Object> body) {
        if (body == null) return null;
        Object data = body.get("data");
        if (data instanceof Map<?, ?> dataMap) {
            Object id = dataMap.get("id");
            return id != null ? id.toString() : null;
        }
        return null;
    }

    private boolean assinaturaValida(String xSignature, String xRequestId, String dataId) {
        if (xSignature == null || xRequestId == null) return false;

        String ts = null;
        String v1 = null;
        for (String parte : xSignature.split(",")) {
            String[] kv = parte.split("=", 2);
            if (kv.length != 2) continue;
            switch (kv[0].trim()) {
                case "ts" -> ts = kv[1].trim();
                case "v1" -> v1 = kv[1].trim();
                default -> { }
            }
        }
        if (ts == null || v1 == null) return false;

        String manifest = "id:" + dataId.toLowerCase() + ";request-id:" + xRequestId + ";ts:" + ts + ";";

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));
            String hashHex = HexFormat.of().formatHex(hash);
            return MessageDigest.isEqual(
                    hashHex.getBytes(StandardCharsets.UTF_8),
                    v1.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            log.error("Erro ao validar assinatura do webhook do Mercado Pago", e);
            return false;
        }
    }
}
