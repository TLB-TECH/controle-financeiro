package br.com.controlefinanceiro.msusuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-lancamentos", url = "${ms-lancamentos.url}")
public interface LancamentoClient {

    @DeleteMapping("/interno/lancamentos/usuarios/{usuarioId}")
    void excluirDadosUsuario(
            @PathVariable String usuarioId,
            @RequestHeader("X-Internal-Secret") String secret
    );
}
