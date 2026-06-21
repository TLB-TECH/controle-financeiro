package br.com.controlefinanceiro.bff.financeiro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.secret=minha-chave-secreta-super-segura-financeiro-2026",
		"jwt.expiration=86400000",
		"spring.cloud.compatibility-verifier.enabled=false",
		"ms-lancamentos.url=http://localhost:8083",
		"ms-centro-custo.url=http://localhost:8082",
		"ms-fluxo-caixa.url=http://localhost:8084"
})
class BffFinanceiroApplicationTests {

	@Test
	void contextLoads() {}
}