package com.tlbtech.ms_orcamento.repository;

import com.tlbtech.ms_orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findByAtivoTrue();

    List<Orcamento> findByMesAndAnoAndAtivoTrue(Integer mes, Integer ano);

    Optional<Orcamento> findByCentroCustoIdAndMesAndAno(Long centroCustoId,Integer mes, Integer ano);

    List<Orcamento> findByCentroCustoIdAndAtivoTrue(Long centroCustoId);
}
