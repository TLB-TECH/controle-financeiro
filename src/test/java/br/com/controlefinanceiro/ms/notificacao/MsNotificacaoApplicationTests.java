package br.com.controlefinanceiro.ms.notificacao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.secret=minha-chave-secreta-super-segura-financeiro-2026",
		"jwt.expiration=86400000",
		"spring.cloud.compatibility-verifier.enabled=false",
		"ms-lancamentos.url=http://localhost:8083",
		"sendgrid.api-key=test-key",
		"sendgrid.from-email=test@test.com",
		"evolution.api-url=http://localhost:8080",
		"evolution.api-key=test-key",
		"evolution.instance=test",
		"internal.secret=test-internal-secret",
		"spring.mail.host=smtp.sendgrid.net",
		"spring.mail.port=587",
		"spring.mail.username=apikey",
		"spring.mail.password=test-key"
})
class MsNotificacaoApplicationTests {

	@Test
	void contextLoads() {}
}