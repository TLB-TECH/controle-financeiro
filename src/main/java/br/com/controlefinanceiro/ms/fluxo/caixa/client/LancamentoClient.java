package br.com.controlefinanceiro.ms.fluxo.caixa.client;

import br.com.controlefinanceiro.ms.fluxo.caixa.dto.LancamentoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ms-lancamentos", url = "${ms-lancamentos.url}")
public interface LancamentoClient {

    @GetMapping("/lancamentos")
    List<LancamentoResponseDTO> listarLancamentos(
            @RequestHeader("Authorization") String token
    );
}
