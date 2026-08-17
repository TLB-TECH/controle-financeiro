package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.dto.AuthRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.AuthResponseDTO;
import br.com.controlefinanceiro.msusuarios.dto.RecuperarSenhaRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.RedefinirSenhaRequestDTO;
import br.com.controlefinanceiro.msusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid AuthRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.autenticar(dto));
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Void> recuperarSenha(
            @RequestBody @Valid RecuperarSenhaRequestDTO dto) {
        usuarioService.recuperarSenha(dto.email());
        return ResponseEntity.ok().build();
    }

            @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @RequestBody @Valid RedefinirSenhaRequestDTO dto) {
        usuarioService.redefinirSenha(dto.token(), dto.novaSenha());
        return ResponseEntity.ok().build();
    }
}