package br.com.controlefinanceiro.msusuarios.service;

import br.com.controlefinanceiro.msusuarios.dto.AcessoDTO;
import br.com.controlefinanceiro.msusuarios.dto.CheckoutResponseDTO;
import br.com.controlefinanceiro.msusuarios.entity.StatusAssinatura;
import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import br.com.controlefinanceiro.msusuarios.exception.PagamentoException;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preapproval.PreApprovalAutoRecurringCreateRequest;
import com.mercadopago.client.preapproval.PreapprovalClient;
import com.mercadopago.client.preapproval.PreapprovalCreateRequest;
import com.mercadopago.resources.preapproval.Preapproval;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssinaturaService {

    private final UsuarioRepository usuarioRepository;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.preco-mensal}")
    private BigDecimal precoMensal;

    @Value("${app.url}")
    private String appUrl;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public AcessoDTO calcularAcesso(Usuario usuario) {
        if (usuario.getStatusAssinatura() == StatusAssinatura.ATIVO) {
            return new AcessoDTO(true, StatusAssinatura.ATIVO.name(), null);
        }

        if (usuario.getStatusAssinatura() == StatusAssinatura.TRIAL) {
            boolean dentroDoTrial = usuario.getTrialFim().isAfter(LocalDateTime.now());
            Long diasRestantes = dentroDoTrial
                    ? Duration.between(LocalDateTime.now(), usuario.getTrialFim()).toDays() + 1
                    : 0L;
            return new AcessoDTO(dentroDoTrial, StatusAssinatura.TRIAL.name(), diasRestantes);
        }

        return new AcessoDTO(false, usuario.getStatusAssinatura().name(), null);
    }

    public CheckoutResponseDTO criarCheckout(Usuario usuario) {
        try {
            PreapprovalClient client = new PreapprovalClient();

            PreapprovalCreateRequest request = PreapprovalCreateRequest.builder()
                    .reason("Assinatura Controle Financeiro")
                    .externalReference(usuario.getId().toString())
                    .payerEmail(usuario.getEmail())
                    .backUrl(appUrl + "/assinatura")
                    .status("pending")
                    .autoRecurring(PreApprovalAutoRecurringCreateRequest.builder()
                            .frequency(1)
                            .frequencyType("months")
                            .transactionAmount(precoMensal)
                            .currencyId("BRL")
                            .build())
                    .build();

            Preapproval preapproval = client.create(request);

            usuario.setMpPreapprovalId(preapproval.getId());
            usuarioRepository.save(usuario);

            return new CheckoutResponseDTO(preapproval.getInitPoint());
        } catch (com.mercadopago.exceptions.MPApiException e) {
            String corpo = e.getApiResponse() != null ? e.getApiResponse().getContent() : "(sem corpo de resposta)";
            log.error("Erro ao criar checkout no Mercado Pago para usuario {}: {}", usuario.getId(), corpo, e);
            throw new PagamentoException("Erro ao iniciar pagamento. Tente novamente.");
        } catch (Exception e) {
            log.error("Erro ao criar checkout no Mercado Pago para usuario {}", usuario.getId(), e);
            throw new PagamentoException("Erro ao iniciar pagamento. Tente novamente.");
        }
    }

    public void processarWebhook(String preapprovalId) {
        try {
            PreapprovalClient client = new PreapprovalClient();
            Preapproval preapproval = client.get(preapprovalId);

            Usuario usuario = usuarioRepository.findByMpPreapprovalId(preapprovalId)
                    .or(() -> buscarPorExternalReference(preapproval.getExternalReference()))
                    .orElse(null);

            if (usuario == null) {
                log.warn("Webhook do Mercado Pago recebido para preapproval {} sem usuario correspondente", preapprovalId);
                return;
            }

            StatusAssinatura novoStatus = switch (preapproval.getStatus()) {
                case "authorized" -> StatusAssinatura.ATIVO;
                case "paused" -> StatusAssinatura.INADIMPLENTE;
                case "cancelled" -> StatusAssinatura.CANCELADO;
                default -> usuario.getStatusAssinatura();
            };

            usuario.setStatusAssinatura(novoStatus);
            usuario.setMpPreapprovalId(preapproval.getId());
            usuario.setAssinaturaAtualizadaEm(LocalDateTime.now());
            usuarioRepository.save(usuario);

            log.info("Assinatura do usuario {} atualizada para {} via webhook", usuario.getId(), novoStatus);
        } catch (Exception e) {
            log.error("Erro ao processar webhook do Mercado Pago para preapproval {}", preapprovalId, e);
        }
    }

    private java.util.Optional<Usuario> buscarPorExternalReference(String externalReference) {
        try {
            return usuarioRepository.findById(UUID.fromString(externalReference));
        } catch (IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }
}
