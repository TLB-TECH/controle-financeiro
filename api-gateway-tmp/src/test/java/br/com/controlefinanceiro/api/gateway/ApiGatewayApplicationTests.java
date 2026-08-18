package br.com.controlefinanceiro.api.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.secret=minha-chave-secreta-super-segura-financeiro-2026",
		"jwt.expiration=86400000",
		"spring.cloud.compatibility-verifier.enabled=false"
})
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
