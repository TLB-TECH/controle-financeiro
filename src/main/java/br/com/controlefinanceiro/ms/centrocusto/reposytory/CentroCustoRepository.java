package br.com.controlefinanceiro.ms.centrocusto.reposytory;

import br.com.controlefinanceiro.ms.centrocusto.model.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {

    List<CentroCusto> findByUsuarioIdAndAtivoTrue(String usuarioId);

    Optional<CentroCusto> findByIdAndUsuarioIdAndAtivoTrue(Long id, String usuarioId);

    boolean existsByNomeAndUsuarioIdAndAtivoTrue(String nome, String usuarioId);
}
