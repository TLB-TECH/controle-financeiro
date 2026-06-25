package br.com.controlefinanceiro.mslancamentos.controller;

import br.com.controlefinanceiro.mslancamentos.dto.LancamentoVencimentoDTO;
import br.com.controlefinanceiro.mslancamentos.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interno")
@RequiredArgsConstructor
public class LancamentoInternoController {

    private final LancamentoService lancamentoService;

    @Value("${internal.secret}")
    private String internalSecret;

    @GetMapping("/lancamentos/vencendo-hoje")
    public ResponseEntity<List<LancamentoVencimentoDTO>> vencendoHoje(
            @RequestHeader("X-Internal-Secret") String secret) {

        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(lancamentoService.buscarVencendoHoje());
    }
}