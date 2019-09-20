package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.service.SecurityService;
import com.tis4.matriz_ensino.model.Turma;
import com.tis4.matriz_ensino.model.accessory.ManipularTurma;
import com.tis4.matriz_ensino.repository.TurmaRepository;

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
public class TurmaController {

    @Autowired
    private TurmaRepository repository;

    @Autowired
    private SecurityService security;

    @GetMapping("/turmas")
    public List<Turma> listarTurmas() {
        return repository.findAll();
    }

    @GetMapping("/turma/{id}")
    public Turma retornarTurma(@PathVariable Long id) {
        Optional<Turma> turma = repository.findById(id);

        if (!turma.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma turma foi encontrada com o ID informado");

        return turma.get();
    }

    @PostMapping("/turma")
    @ResponseStatus(HttpStatus.CREATED)
    public Turma salvarTurma(@RequestBody @Valid Turma turma) {
        return repository.save(turma);
    }

    @PutMapping("/turma")
    public Turma atualizarTurma(@RequestBody @Valid ManipularTurma request) {

        if (request.getId() != null) {
            Optional<Turma> turmaAtual = repository.findById(request.getId());

            if (turmaAtual.isPresent()) {

                if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {
                    Turma turmaAtualizada = new Turma(request.getId(), request.getNome(), request.getSupervisor(),
                            request.getProfessor());

                    return salvarTurma(turmaAtualizada);
                }
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Falha de Autenticação: você não tem permissão para alterar os dados desta turma");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nenhuma turma foi encontrada com o ID informado");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "É necessário informar um ID de turma para realizar alterações");
    }

    @DeleteMapping("/turma")
    public void deletarTurma(@RequestBody ManipularTurma request) {

        if (request.getId() != null) {
            Optional<Turma> turmaAtual = repository.findById(request.getId());

            if (turmaAtual.isPresent()) {

                if (security.permissaoAdmin(request.getUsernameAdm(), request.getSenhaAdm())) {
                    Turma turma = repository.findById(request.getId()).get();
                    if (turma.getNome().equals(request.getNome()))
                        repository.delete(turma);
                    else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Erro de Integridade: ID e NOME não correspondem.");
                } else
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "Falha de Autenticação: você não tem permissão para excluir esta turma");
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nenhuma turma foi encontrada com o ID informado");
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de turma para realizar a exclusão");
    }
}