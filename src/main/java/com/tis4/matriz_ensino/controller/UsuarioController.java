package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.service.SecurityService;
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

    @Autowired
    private SecurityService security;

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
        Optional<Usuario> usernameExistente = repository.findByUsername(usuario.getUsername());
        Optional<Usuario> idExistente = Optional.empty();

        if (usuario.getId() != null)
            idExistente = repository.findById(usuario.getId());

        if (usernameExistente.isPresent() && usernameExistente.get().getId() != usuario.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Já existe um usuário com o USERNAME informado, tente outro");

        else if (!usernameExistente.isPresent() && !idExistente.isPresent()
                || usernameExistente.isPresent() && !usernameExistente.get().getSenha().equals(usuario.getSenha()))
            usuario.setSenha(security.crypto(usuario.getSenha()));

        return repository.save(usuario);
    }

    @PutMapping("/usuario")
    public Usuario atualizarUsuario(@RequestBody @Valid ManipularUsuario request) {

        if (request.getId() != null) {
            Optional<Usuario> usuarioAtual = repository.findById(request.getId());

            if (usuarioAtual.isPresent()) {

                if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {
                    Usuario usuarioAtualizado;
                    if (request.getSenha() == null
                            || request.getSenha() != null && request.getSenha().equals(usuarioAtual.get().getSenha()))
                        usuarioAtualizado = new Usuario(request.getId(), request.getUsername(),
                                request.getNomeCompleto(), usuarioAtual.get().getSenha(), request.getTipo());
                    else
                        usuarioAtualizado = new Usuario(request.getId(), request.getUsername(),
                                request.getNomeCompleto(), request.getSenha(), request.getTipo());

                    return salvarUsuario(usuarioAtualizado);
                }
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Falha de Autenticação: você não tem permissão para alterar os dados deste usuário");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nenhum usuário foi encontrado com o ID informado");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "É necessário informar um ID de usuário para realizar alterações");
    }

    @DeleteMapping("/usuario")
    public void deletarUsuario(@RequestBody @Valid ManipularUsuario request) {

        if (request.getId() != null) {
            Optional<Usuario> usuarioAtual = repository.findById(request.getId());

            if (usuarioAtual.isPresent()) {

                if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {
                    Usuario usuario = repository.findById(request.getId()).get();
                    if (usuario.getUsername().equals(request.getUsername()))
                        repository.delete(usuario);
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Erro de Integridade: ID e USERNAME não correspondem.");
                } else
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "Falha de Autenticação: você não tem permissão para excluir este usuário");
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nenhum usuário foi encontrado com o ID informado");
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de usuário para realizar a exclusão");
    }

    @PostMapping("/usuario/auth")
    public Usuario autenticarUsuario(@RequestBody @Valid AutenticarUsuario login) {
        Optional<Usuario> usuario = repository.findByUsername(login.getUsername());

        if (usuario.isPresent()) {
            if (usuario.get().getSenha().equals(security.crypto(login.getSenha())))
                return usuario.get();
            else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "SENHA e USERNAME informados não correspondem");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum usuário foi encontrado com o USERNAME informado");
    }

}