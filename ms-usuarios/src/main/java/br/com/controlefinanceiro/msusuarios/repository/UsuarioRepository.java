package br.com.controlefinanceiro.msusuarios.repository;

import br.com.controlefinanceiro.msusuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email);

    List<Usuario> findAllByAtivoTrue();

    Optional<Usuario> findByEmail(String email);

}
