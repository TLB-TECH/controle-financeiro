package com.tlbtech.ms_contas.repository;

import com.tlbtech.ms_contas.model.CategoriaTipoConta;
import com.tlbtech.ms_contas.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByAtivoTrue();

    @Query("SELECT COALESCE(SUM(c.saldo), 0) FROM Conta c WHERE c.ativo = true AND c.tipoConta.categoria = :categoria")
    BigDecimal sumSaldoByCategoria(CategoriaTipoConta categoria);
}
