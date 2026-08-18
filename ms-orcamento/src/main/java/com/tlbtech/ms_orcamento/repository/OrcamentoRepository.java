package com.tlbtech.ms_orcamento.repository;

import com.tlbtech.ms_orcamento.model.Orcamento;
import com.tlbtech.ms_orcamento.model.TipoAlvoOrcamento;
import com.tlbtech.ms_orcamento.model.TipoOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findByUsuarioIdAndAtivoTrue(String usuarioId);

    Optional<Orcamento> findByIdAndUsuarioId(Long id, String usuarioId);

    /** Vigentes no mês/ano informados: MENSAL do mês exato, ANUAL do ano, e DEFINITIVO (sempre vigente).
     *  Alvo-agnóstico — vale tanto pra centro de custo quanto pra cartão específico ou todos os cartões. */
    @Query("SELECT o FROM Orcamento o WHERE o.usuarioId = :usuarioId AND o.ativo = true AND (" +
           "(o.tipo = com.tlbtech.ms_orcamento.model.TipoOrcamento.MENSAL AND o.mes = :mes AND o.ano = :ano) OR " +
           "(o.tipo = com.tlbtech.ms_orcamento.model.TipoOrcamento.ANUAL AND o.ano = :ano) OR " +
           "(o.tipo = com.tlbtech.ms_orcamento.model.TipoOrcamento.DEFINITIVO))")
    List<Orcamento> findVigentesPorMesAno(@Param("usuarioId") String usuarioId, @Param("mes") Integer mes, @Param("ano") Integer ano);

    Optional<Orcamento> findByUsuarioIdAndCentroCustoIdAndMesAndAnoAndTipo(String usuarioId, Long centroCustoId, Integer mes, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndCentroCustoIdAndAnoAndTipo(String usuarioId, Long centroCustoId, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndCentroCustoIdAndTipoAndAtivoTrue(String usuarioId, Long centroCustoId, TipoOrcamento tipo);

    List<Orcamento> findByUsuarioIdAndCentroCustoIdAndAtivoTrue(String usuarioId, Long centroCustoId);

    Optional<Orcamento> findByUsuarioIdAndCartaoCreditoIdAndMesAndAnoAndTipo(String usuarioId, Long cartaoCreditoId, Integer mes, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndCartaoCreditoIdAndAnoAndTipo(String usuarioId, Long cartaoCreditoId, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndCartaoCreditoIdAndTipoAndAtivoTrue(String usuarioId, Long cartaoCreditoId, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndTipoAlvoAndMesAndAnoAndTipo(String usuarioId, TipoAlvoOrcamento tipoAlvo, Integer mes, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndTipoAlvoAndAnoAndTipo(String usuarioId, TipoAlvoOrcamento tipoAlvo, Integer ano, TipoOrcamento tipo);

    Optional<Orcamento> findByUsuarioIdAndTipoAlvoAndTipoAndAtivoTrue(String usuarioId, TipoAlvoOrcamento tipoAlvo, TipoOrcamento tipo);

    void deleteByUsuarioId(String usuarioId);
}
