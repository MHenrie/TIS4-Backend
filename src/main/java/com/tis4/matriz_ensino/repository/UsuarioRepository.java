package com.tis4.matriz_ensino.repository;

import com.tis4.matriz_ensino.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findById(long id);

    Usuario findByUsername(String username);
}