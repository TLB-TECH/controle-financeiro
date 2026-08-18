package br.com.controlefinanceiro.ms.notificacao.controller;

import br.com.controlefinanceiro.ms.notificacao.dto.PreferenciaNotificacaoRequestDTO;
import br.com.controlefinanceiro.ms.notificacao.dto.PreferenciaNotificacaoResponseDTO;
import br.com.controlefinanceiro.ms.notificacao.service.PreferenciaNotificacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferencias")
@RequiredArgsConstructor
public class PreferenciaNotificacaoController {

    private final PreferenciaNotificacaoService service;

    @PostMapping
    public ResponseEntity<PreferenciaNotificacaoResponseDTO> salvar(
            @AuthenticationPrincipal String usuarioId,
            @Valid @RequestBody PreferenciaNotificacaoRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(usuarioId, dto));
    }

    @GetMapping
    public ResponseEntity<PreferenciaNotificacaoResponseDTO> buscar(
            @AuthenticationPrincipal String usuarioId) {
        return ResponseEntity.ok(service.buscar(usuarioId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal String usuarioId) {
        service.deletar(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
