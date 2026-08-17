package br.com.controlefinanceiro.ms.centrocusto.controller;

import br.com.controlefinanceiro.ms.centrocusto.service.CentroCustoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno")
@RequiredArgsConstructor
public class CentroCustoInternoController {

    private final CentroCustoService centroCustoService;

    @Value("${internal.secret}")
    private String internalSecret;

    @DeleteMapping("/centros-custo/usuarios/{usuarioId}")
    public ResponseEntity<Void> excluirDadosUsuario(
            @PathVariable String usuarioId,
            @RequestHeader("X-Internal-Secret") String secret) {

        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        centroCustoService.excluirDadosUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
