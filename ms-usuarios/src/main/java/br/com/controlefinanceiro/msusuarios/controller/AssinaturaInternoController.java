package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.dto.AcessoDTO;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import br.com.controlefinanceiro.msusuarios.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno/usuarios")
@RequiredArgsConstructor
public class AssinaturaInternoController {

    private final AssinaturaService assinaturaService;
    private final UsuarioRepository usuarioRepository;

    @Value("${internal.secret}")
    private String internalSecret;

    @GetMapping("/{email}/acesso")
    public ResponseEntity<AcessoDTO> acesso(
            @PathVariable String email,
            @RequestHeader("X-Internal-Secret") String secret) {

        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return usuarioRepository.findByEmail(email)
                .map(usuario -> ResponseEntity.ok(assinaturaService.calcularAcesso(usuario)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
