package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.service.DataIntegrityService;
import com.tis4.matriz_ensino.service.SecurityService;
import com.tis4.matriz_ensino.model.Usuario;
import com.tis4.matriz_ensino.model.accessory.AutenticarUsuario;
import com.tis4.matriz_ensino.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/usuario")
    public Usuario retornarUsuario(@RequestParam("username") String username) {
        Optional<Usuario> usuario = repository.findByUsername(username);

        if (!usuario.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum usuário encontrado com o USERNAME informado.");

        return usuario.get();
    }

    @PostMapping("/usuario")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvarUsuario(@RequestBody @Valid Usuario usuario, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm)) {

            if (usuario.getId() == null) {

                if (dataIntegrity.usernameDisponivel(usuario.getUsername()))
                    return repository.save(usuario);

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Já existe um usuário com o USERNAME informado, tente outro.");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar um novo usuário.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novos usuários.");
    }

    @PutMapping("/usuario")
    public Usuario atualizarUsuario(@RequestBody @Valid Usuario usuario, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm)) {

            if (usuario.getId() != null) {
                Optional<Usuario> usuarioAtual = repository.findById(usuario.getId());

                if (usuarioAtual.isPresent()) {

                    if (dataIntegrity.usernameEqualsId(usuario.getUsername(), usuario.getId())
                            || dataIntegrity.usernameDisponivel(usuario.getUsername())) {

                        if (!usuario.getSenha().equals(usuarioAtual.get().getSenha()))
                            usuario.setSenha(security.crypto(usuario.getSenha()));

                        return repository.save(usuario);
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Já existe um usuário com o USERNAME informado, tente outro.");
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nenhum usuário foi encontrado com o ID informado.");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de usuário para realizar alterações.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de usuários.");
    }

    @DeleteMapping("/usuario")
    public void deletarUsuario(@RequestParam("id") Long id, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm))
            repository.deleteById(id);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir usuários.");
    }

    @PostMapping("/usuario/auth")
    public Usuario autenticarUsuario(@RequestBody @Valid AutenticarUsuario login) {
        Optional<Usuario> usuario = repository.findByUsername(login.getUsername());

        if (usuario.isPresent()) {
            if (usuario.get().getSenha().equals(security.crypto(login.getSenha())))
                return usuario.get();
            else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "SENHA e USERNAME informados não correspondem.");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum usuário foi encontrado com o USERNAME informado.");
    }

    @GetMapping("/professores")
    public List<Usuario> listarProfessores() {
        return repository.findAllByTipo("Professor");
    }

    @GetMapping("/supervisores")
    public List<Usuario> listarSupervisores() {
        return repository.findAllByTipo("Supervisor");
    }
}