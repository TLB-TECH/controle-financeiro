package com.tlbtech.ms_orcamento.controller;

import com.tlbtech.ms_orcamento.service.MetaAplicacaoService;
import com.tlbtech.ms_orcamento.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno")
public class OrcamentoInternoController {

    private final OrcamentoService orcamentoService;
    private final MetaAplicacaoService metaAplicacaoService;

    @Value("${internal.secret}")
    private String internalSecret;

    public OrcamentoInternoController(OrcamentoService orcamentoService, MetaAplicacaoService metaAplicacaoService) {
        this.orcamentoService = orcamentoService;
        this.metaAplicacaoService = metaAplicacaoService;
    }

    @DeleteMapping("/orcamentos/usuarios/{usuarioId}")
    public ResponseEntity<Void> excluirDadosUsuario(
            @PathVariable String usuarioId,
            @RequestHeader("X-Internal-Secret") String secret) {

        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        orcamentoService.excluirDadosUsuario(usuarioId);
        metaAplicacaoService.excluirDadosUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
