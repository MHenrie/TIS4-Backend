package com.tis4.matriz_ensino.service;

import java.util.Optional;

import com.tis4.matriz_ensino.model.Usuario;
import com.tis4.matriz_ensino.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataIntegrityService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean usernameDisponivel(String username) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(username);

        if (usuarioExistente.isPresent())
            return false;

        return true;
    }

    public boolean usernameEqualsId(String username, Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsernameAndId(username, id);

        return usuarioExistente.isPresent() ? true : false;
    }

}