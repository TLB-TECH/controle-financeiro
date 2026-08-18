package br.com.controlefinanceiro.ms.notificacao.client;

import br.com.controlefinanceiro.ms.notificacao.dto.LancamentoVencimentoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ms-lancamentos", url = "${ms-lancamentos.url}")
public interface LancamentoClient {

    @GetMapping("/interno/lancamentos/vencendo-hoje")
    List<LancamentoVencimentoDTO> buscarLancamentosVencendoHoje(
            @RequestHeader("X-Internal-Secret") String internalSecret
    );
}
