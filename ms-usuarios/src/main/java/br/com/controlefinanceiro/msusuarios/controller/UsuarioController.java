package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.dto.AlterarSenhaRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.UsuarioRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.UsuarioResponseDTO;
import br.com.controlefinanceiro.msusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(
            @RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.criar(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> meuPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PutMapping("/me/senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestBody @Valid AlterarSenhaRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        usuarioService.alterarSenha(email, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> excluirContaPropria() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        usuarioService.excluirContaPropria(email);
        return ResponseEntity.noContent().build();
    }
}
