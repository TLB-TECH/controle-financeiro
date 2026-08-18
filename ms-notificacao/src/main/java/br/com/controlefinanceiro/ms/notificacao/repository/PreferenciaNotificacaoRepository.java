package br.com.controlefinanceiro.ms.notificacao.repository;

import br.com.controlefinanceiro.ms.notificacao.model.PreferenciaNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenciaNotificacaoRepository extends JpaRepository<PreferenciaNotificacao, Long> {

    Optional<PreferenciaNotificacao> findByUsuarioIdAndAtivoTrue(String usuarioId);

    List<PreferenciaNotificacao> findAllByHoraNotificacaoAndAtivoTrue(Integer horaNotificacao);


}
