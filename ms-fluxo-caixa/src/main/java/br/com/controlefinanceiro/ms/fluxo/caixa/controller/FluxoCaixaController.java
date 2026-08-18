package br.com.controlefinanceiro.ms.fluxo.caixa.controller;

import br.com.controlefinanceiro.ms.fluxo.caixa.dto.FluxoCaixaResponseDTO;
import br.com.controlefinanceiro.ms.fluxo.caixa.service.FluxoCaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fluxo-caixa")
@RequiredArgsConstructor
public class FluxoCaixaController {

    private final FluxoCaixaService fluxoCaixaService;

    @GetMapping
    public ResponseEntity<FluxoCaixaResponseDTO> calcular(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(fluxoCaixaService.calcular(token));
    }
}
