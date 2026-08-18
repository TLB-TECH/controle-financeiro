package br.com.controlefinanceiro.mslancamentos.client;

import br.com.controlefinanceiro.mslancamentos.dto.LancamentoBancarioRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-contas", url = "${ms-contas.url}")
public interface ContaClient {

    @PostMapping("/contas/{id}/lancamentos")
    Object registrarLancamento(
            @PathVariable Long id,
            @RequestBody LancamentoBancarioRequestDTO dto,
            @RequestHeader("Authorization") String token
    );
}
