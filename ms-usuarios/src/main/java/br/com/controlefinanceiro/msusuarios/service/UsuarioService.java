package br.com.controlefinanceiro.msusuarios.service;

import br.com.controlefinanceiro.msusuarios.client.CentroCustoClient;
import br.com.controlefinanceiro.msusuarios.client.ContaClient;
import br.com.controlefinanceiro.msusuarios.client.LancamentoClient;
import br.com.controlefinanceiro.msusuarios.client.OrcamentoClient;
import br.com.controlefinanceiro.msusuarios.dto.*;
import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import br.com.controlefinanceiro.msusuarios.exception.CredenciaisInvalidasException;
import br.com.controlefinanceiro.msusuarios.exception.EmailJaCadastradoException;
import br.com.controlefinanceiro.msusuarios.exception.UsuarioNaoEncontradoException;
import br.com.controlefinanceiro.msusuarios.repository.UsuarioRepository;
import br.com.controlefinanceiro.msusuarios.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final LancamentoClient lancamentoClient;
    private final CentroCustoClient centroCustoClient;
    private final ContaClient contaClient;
    private final OrcamentoClient orcamentoClient;

    @Value("${internal.secret}")
    private String internalSecret;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException("E-mail já está em uso");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .trialFim(LocalDateTime.now().plusDays(7))
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        if (!usuario.getAtivo()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");
        }
        return toResponse(usuario);
    }

    public AuthResponseDTO autenticar(AuthRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .filter(Usuario::getAtivo)
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getEmail());
        return new AuthResponseDTO(token, "Bearer");
    }

    // - Recuperar senha -
    public void recuperarSenha(String email) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        if (opt.isEmpty()) return;

        String token = UUID.randomUUID().toString();
        Usuario usuario = opt.get();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiracao(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        emailService.enviarEmailRecuperacao(email, token);
    }

    // - Redefinir senha via token -
    public void redefinirSenha(String token, String novaSenha) {
        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (usuario.getResetTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiracao(null);
        usuarioRepository.save(usuario);
    }

    // - Alterar senha (usuário autenticado) -
    public void alterarSenha(String emailAutenticado, AlterarSenhaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    // - Excluir própria conta - apaga de vez o registro (não é soft delete), pra liberar o
    // e-mail imediatamente pra um novo cadastro, e manda apagar em cascata todo o histórico
    // desse usuário nos outros serviços (lançamentos, cartões, centros de custo, contas
    // bancárias, tipos de conta, orçamentos e metas de aplicação).
    public void excluirContaPropria(String emailAutenticado) {
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        lancamentoClient.excluirDadosUsuario(emailAutenticado, internalSecret);
        centroCustoClient.excluirDadosUsuario(emailAutenticado, internalSecret);
        contaClient.excluirDadosUsuario(emailAutenticado, internalSecret);
        orcamentoClient.excluirDadosUsuario(emailAutenticado, internalSecret);

        usuarioRepository.delete(usuario);
    }

    private UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getAtivo(), u.getDataCriacao());
    }

}

