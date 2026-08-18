package com.tlbtech.ms_orcamento.repository;

import com.tlbtech.ms_orcamento.model.MetaAplicacaoAnual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaAplicacaoAnualRepository extends JpaRepository<MetaAplicacaoAnual, Long> {

    Optional<MetaAplicacaoAnual> findByUsuarioIdAndAno(String usuarioId, Integer ano);

    void deleteByUsuarioId(String usuarioId);
}
