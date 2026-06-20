package br.com.controlefinanceiro.mslancamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsLancamentosApplication {

	public static void main(String[] args) {

		SpringApplication.run(MsLancamentosApplication.class, args);
	}

}
