package br.com.controlefinanceiro.msusuarios.controller;

import br.com.controlefinanceiro.msusuarios.dto.AuthRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.AuthResponseDTO;
import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import br.com.controlefinanceiro.msusuarios.exception.UsuarioNaoEncontradoException;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import br.com.controlefinanceiro.msusuarios.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(
            @RequestBody @Valid AuthRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer"));
    }
}
