package com.tis4.matriz_ensino.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.implement.Security;
import com.tis4.matriz_ensino.model.Usuario;
import com.tis4.matriz_ensino.model.accessory.AutenticarUsuario;
import com.tis4.matriz_ensino.model.accessory.ManipularUsuario;
import com.tis4.matriz_ensino.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    @GetMapping("/usuario/{username}")
    public Usuario retornarUsuario(@PathVariable String username) {
        Optional<Usuario> usuario = repository.findByUsername(username);

        if (!usuario.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum usuário encontrado com o USERNAME informado");

        return usuario.get();
    }

    @PostMapping("/usuario")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvarUsuario(@RequestBody @Valid Usuario usuario) {
        Optional<Usuario> usuarioExistente = repository.findByUsername(usuario.getUsername());

        if (usuarioExistente.isPresent() && usuarioExistente.get().getId() != usuario.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Já existe um usuário com o USERNAME informado, tente outro");

        return repository.save(usuario);
    }

    @PutMapping("/usuario")
    public Usuario atualizarUsuario(@RequestBody @Valid ManipularUsuario request)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        if (request.getId() != null) {
            Optional<Usuario> usuarioAtual = repository.findById(request.getId());

            if (usuarioAtual.isPresent()) {

                if (temPermissao(request.getUsernameAdm(), request.getSenhaAdm())) {
                    Usuario usuarioAtualizado;
                    if (request.getSenha() == null)
                        usuarioAtualizado = new Usuario(request.getId(), request.getUsername(),
                                request.getNomeCompleto(), usuarioAtual.get().getSenha(), request.getTipo());
                    else
                        usuarioAtualizado = new Usuario(request.getId(), request.getUsername(),
                                request.getNomeCompleto(), Security.crypto(request.getSenha()), request.getTipo());

                    return salvarUsuario(usuarioAtualizado);
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Falha de Autenticação: você não tem permissão para alterar os dados deste usuário");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nenhum usuário foi encontrado com o ID informado");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "É necessário informar um ID de usuário para realizar alterações");
    }

    @DeleteMapping("/usuario")
    public void deletarUsuario(@RequestBody @Valid ManipularUsuario request)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        if (temPermissao(request.getUsernameAdm(), request.getSenhaAdm())) {
            Usuario usuario = repository.findById(request.getId()).get();

            repository.delete(usuario);

        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Falha de Autenticação: você não tem permissão para excluir este usuário");
    }

    @PostMapping("/usuario/auth")
    public Usuario autenticarUsuario(@RequestBody @Valid AutenticarUsuario login)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Optional<Usuario> usuario = repository.findByUsername(login.getUsername());

        if (usuario.isPresent()) {
            if (usuario.get().getSenha().equals(Security.crypto(login.getSenha())))
                return usuario.get();
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SENHA e USERNAME não correspondem");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum usuário foi encontrado com o USERNAME informado");
    }

    private boolean temPermissao(String username, String senha)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Optional<Usuario> administrador = repository.findByUsername(username);

        if (administrador.isPresent()) {
            if (administrador.get().getTipo().equals("Administrador")) {
                if (administrador.get().getSenha().equals(Security.crypto(senha)))
                    return true;
            }
        }
        return false;
    }

}