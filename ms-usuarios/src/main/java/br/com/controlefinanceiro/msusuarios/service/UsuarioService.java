package br.com.controlefinanceiro.msusuarios.service;

import br.com.controlefinanceiro.msusuarios.dto.AuthRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.AuthResponseDTO;
import br.com.controlefinanceiro.msusuarios.dto.UsuarioRequestDTO;
import br.com.controlefinanceiro.msusuarios.dto.UsuarioResponseDTO;
import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import br.com.controlefinanceiro.msusuarios.exception.CredenciaisInvalidasException;
import br.com.controlefinanceiro.msusuarios.exception.EmailJaCadastradoException;
import br.com.controlefinanceiro.msusuarios.exception.UsuarioNaoEncontradoException;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import br.com.controlefinanceiro.msusuarios.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException("E-mail já está em uso");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO buscarPorId(UUID id) {
        return toResponse(usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id)));
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(dto.email()) &&
                usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException("E-mail já em uso");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        return toResponse(usuarioRepository.save(usuario));
    }

    public void desativar(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public AuthResponseDTO autenticar(AuthRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getEmail());
        return new AuthResponseDTO(token, "Bearer");
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }
}
