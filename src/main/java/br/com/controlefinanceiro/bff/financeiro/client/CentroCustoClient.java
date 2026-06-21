package br.com.controlefinanceiro.bff.financeiro.client;

import br.com.controlefinanceiro.bff.financeiro.dto.CentroCustoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ms-centro-custo", url = "${ms-centro-custo.url}")
public interface CentroCustoClient {

    @GetMapping("/centros-custo")
    List<CentroCustoResponseDTO> listarCentrosCusto(
            @RequestHeader("Authorization") String token
    );
}
