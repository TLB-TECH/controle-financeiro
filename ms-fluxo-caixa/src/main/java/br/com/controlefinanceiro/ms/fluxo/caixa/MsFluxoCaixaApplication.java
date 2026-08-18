package br.com.controlefinanceiro.ms.fluxo.caixa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsFluxoCaixaApplication {

	public static void main(String[] args) {

		SpringApplication.run(MsFluxoCaixaApplication.class, args);
	}

}
