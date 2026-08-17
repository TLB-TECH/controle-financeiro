package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.dto.AcessoDTO;
import br.com.controlefinanceiro.msusuarios.dto.CheckoutResponseDTO;
import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import br.com.controlefinanceiro.msusuarios.exception.UsuarioNaoEncontradoException;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import br.com.controlefinanceiro.msusuarios.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/me/assinatura")
@RequiredArgsConstructor
public class AssinaturaController {

    private final AssinaturaService assinaturaService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<AcessoDTO> status() {
        return ResponseEntity.ok(assinaturaService.calcularAcesso(usuarioAutenticado()));
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout() {
        return ResponseEntity.ok(assinaturaService.criarCheckout(usuarioAutenticado()));
    }

    private Usuario usuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }
}
