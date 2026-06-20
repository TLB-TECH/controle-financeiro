package br.com.controlefinanceiro.ms.fluxo.caixa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"ms-lancamentos.url=http://localhost:8083",
		"jwt.secret=minha-chave-secreta-super-segura-financeiro-2026",
		"jwt.expiration=86400000"
})
class MsFluxoCaixaApplicationTests {

	@Test
	void contextLoads() {
	}

}