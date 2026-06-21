package br.com.controlefinanceiro.bff.financeiro.client;

import br.com.controlefinanceiro.bff.financeiro.dto.FluxoCaixaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-fluxo-caixa", url = "${ms-fluxo-caixa.url}")
public interface FluxoCaixaClient {

    @GetMapping("/fluxo-caixa")
    FluxoCaixaResponseDTO calcular(
            @RequestHeader("Authorization") String token
    );
}
