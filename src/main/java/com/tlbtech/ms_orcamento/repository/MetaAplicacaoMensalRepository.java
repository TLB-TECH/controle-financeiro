package com.tlbtech.ms_orcamento.repository;

import com.tlbtech.ms_orcamento.model.MetaAplicacaoMensal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaAplicacaoMensalRepository extends JpaRepository<MetaAplicacaoMensal, Long> {

    List<MetaAplicacaoMensal> findByUsuarioIdAndAno(String usuarioId, Integer ano);

    Optional<MetaAplicacaoMensal> findByUsuarioIdAndMesAndAno(String usuarioId, Integer mes, Integer ano);

    void deleteByUsuarioId(String usuarioId);
}
