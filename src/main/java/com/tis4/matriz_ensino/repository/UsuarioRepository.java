package com.tis4.matriz_ensino.repository;

import java.util.Optional;

import com.tis4.matriz_ensino.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByUsernameAndId(String username, Long id);

}