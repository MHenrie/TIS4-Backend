package com.tis4.matriz_ensino.controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

import com.tis4.matriz_ensino.Security;
import com.tis4.matriz_ensino.models.FormLogin;
import com.tis4.matriz_ensino.models.Usuario;
import com.tis4.matriz_ensino.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/usuarios")
    public List<Usuario> listaUsuarios() {
        return repository.findAll();
    }

    @GetMapping("/usuario/{username}")
    public Usuario retornaUsuario(@PathVariable(value = "username") String username) {
        return repository.findByUsername(username);
    }

    @PostMapping("/usuario")
    public Usuario salvaUsuario(@RequestBody @Valid Usuario usuario) {
        return repository.save(usuario);
    }

    @PutMapping("/usuario")
    public Usuario atualizaUsuario(@RequestBody @Valid Usuario usuario) {
        return repository.save(usuario);
    }

    @DeleteMapping("/usuario")
    public void deletaUsuario(@RequestBody Usuario usuario) {
        repository.delete(usuario);
    }

    @PostMapping("/usuario/auth")
    public Usuario autenticaUsuario(@RequestBody @Valid FormLogin login)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Usuario usuario = repository.findByUsername(login.getUsername());
        if (usuario != null) {
            if (usuario.getSenha().equals(Security.crypto(login.getSenha())))
                return usuario;
        }
        return null;
    }

}