package br.com.controlefinanceiro.mslancamentos.repository;

import br.com.controlefinanceiro.mslancamentos.entity.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioIdAndAtivoTrue(String usuarioId);
    Optional<CartaoCredito> findByIdAndUsuarioIdAndAtivoTrue(Long id, String usuarioId);
}