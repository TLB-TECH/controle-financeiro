package br.com.controlefinanceiro.mslancamentos.client;

import br.com.controlefinanceiro.mslancamentos.dto.CentroCustoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-centro-custo", url = "${ms-centro-custo.url}")
public interface CentroCustoClient {

    @GetMapping("/centros-custo/{id}")
    CentroCustoResponseDTO buscarPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );
}
