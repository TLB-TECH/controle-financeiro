package br.com.controlefinanceiro.mslancamentos.service;

import br.com.controlefinanceiro.mslancamentos.dto.CartaoCreditoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.CartaoCreditoResponseDTO;
import br.com.controlefinanceiro.mslancamentos.entity.CartaoCredito;
import br.com.controlefinanceiro.mslancamentos.exception.LancamentoNaoEncontradoException;
import br.com.controlefinanceiro.mslancamentos.repository.CartaoCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository repository;

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public CartaoCreditoResponseDTO criar(CartaoCreditoRequestDTO dto) {
        CartaoCredito cartao = CartaoCredito.builder()
                .nome(dto.nome())
                .limite(dto.limite())
                .usuarioId(getEmailAutenticado())
                .build();
        return CartaoCreditoResponseDTO.fromEntity(repository.save(cartao));
    }

    public List<CartaoCreditoResponseDTO> listar() {
        return repository.findByUsuarioIdAndAtivoTrue(getEmailAutenticado())
                .stream()
                .map(CartaoCreditoResponseDTO::fromEntity)
                .toList();
    }

    public CartaoCreditoResponseDTO buscarPorId(Long id) {
        return CartaoCreditoResponseDTO.fromEntity(
                repository.findByIdAndUsuarioIdAndAtivoTrue(id, getEmailAutenticado())
                        .orElseThrow(() -> new LancamentoNaoEncontradoException("Cartão não encontrado"))
        );
    }

    public CartaoCreditoResponseDTO atualizar(Long id, CartaoCreditoRequestDTO dto) {
        CartaoCredito cartao = repository.findByIdAndUsuarioIdAndAtivoTrue(id, getEmailAutenticado())
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Cartão não encontrado"));
        cartao.setNome(dto.nome());
        cartao.setLimite(dto.limite());
        return CartaoCreditoResponseDTO.fromEntity(repository.save(cartao));
    }

    public void deletar(Long id) {
        CartaoCredito cartao = repository.findByIdAndUsuarioIdAndAtivoTrue(id, getEmailAutenticado())
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Cartão não encontrado"));
        cartao.setAtivo(false);
        repository.save(cartao);
    }
}