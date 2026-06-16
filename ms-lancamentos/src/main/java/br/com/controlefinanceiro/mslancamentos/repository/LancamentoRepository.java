package br.com.controlefinanceiro.mslancamentos.repository;

import br.com.controlefinanceiro.mslancamentos.entity.Lancamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByUsuarioIdAndAtivoTrue(String usuarioId);

    List<Lancamento>  findByUsuarioIdAndTipoAndAtivoTrue(String usuarioId, TipoLancamento tipo);

    List<Lancamento> findByUsuarioIdAndStatusAndAtivoTrue(String usuarioId, StatusLancamento status);

    Optional<Lancamento> findByIdAndAtivoTrue(Long id);

}
