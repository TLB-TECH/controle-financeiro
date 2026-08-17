package br.com.controlefinanceiro.msusuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-centro-custo", url = "${ms-centro-custo.url}")
public interface CentroCustoClient {

    @DeleteMapping("/interno/centros-custo/usuarios/{usuarioId}")
    void excluirDadosUsuario(
            @PathVariable String usuarioId,
            @RequestHeader("X-Internal-Secret") String secret
    );
}
