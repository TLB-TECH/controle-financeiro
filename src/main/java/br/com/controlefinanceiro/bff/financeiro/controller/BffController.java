package br.com.controlefinanceiro.bff.financeiro.controller;

import br.com.controlefinanceiro.bff.financeiro.dto.DashboardResponseDTO;
import br.com.controlefinanceiro.bff.financeiro.service.BffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class BffController {

    private final BffService bffService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(bffService.getDashboard(token));
    }
}
