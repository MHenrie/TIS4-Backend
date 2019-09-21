package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.service.DataIntegrityService;
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
    @Autowired
    private DataIntegrityService dataIntegrity;

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
    public Usuario salvarUsuario(@RequestBody @Valid ManipularUsuario request) {

        if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {

            if (request.getId() == null) {

                if (dataIntegrity.usernameDisponivel(request.getUsername())) {

                    Usuario usuario = new Usuario(request.getUsername(), request.getNomeCompleto(),
                            security.crypto(request.getSenha()), request.getTipo());

                    return repository.save(usuario);
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Já existe um usuário com o USERNAME informado, tente outro");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar um novo usuário");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novos usuários");
    }

    @PutMapping("/usuario")
    public Usuario atualizarUsuario(@RequestBody @Valid ManipularUsuario request) {

        if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {

            if (request.getId() != null) {
                Optional<Usuario> usuarioAtual = repository.findById(request.getId());

                if (usuarioAtual.isPresent()) {

                    if (dataIntegrity.usernameEqualsId(request.getUsername(), request.getId())
                            || dataIntegrity.usernameDisponivel(request.getUsername())) {

                        Usuario usuarioAtualizado = new Usuario(request.getId(), request.getUsername(),
                                request.getNomeCompleto(), request.getSenha(), request.getTipo());

                        if (!request.getSenha().equals(usuarioAtual.get().getSenha()))
                            usuarioAtualizado.setSenha(security.crypto(request.getSenha()));

                        return repository.save(usuarioAtualizado);
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Já existe um usuário com o USERNAME informado, tente outro");
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nenhum usuário foi encontrado com o ID informado");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de usuário para realizar alterações");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de usuários");
    }

    @DeleteMapping("/usuario")
    public void deletarUsuario(@RequestBody @Valid ManipularUsuario request) {

        if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {

            if (request.getId() != null) {

                if (dataIntegrity.usernameEqualsId(request.getUsername(), request.getId()))
                    repository.deleteById(request.getId());

                else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Erro de Integridade: ID e USERNAME não correspondem");
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "É necessário informar um ID de usuário para realizar uma exclusão");
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir usuários");
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