package com.tis4.matriz_ensino.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.models.Disciplina;
import com.tis4.matriz_ensino.repositories.DisciplinaRepository;
import com.tis4.matriz_ensino.services.SecurityService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repository;
    @Autowired
    private SecurityService security;

    @GetMapping("/disciplinas")
    public List<Disciplina> listarDisciplinas() {
        return repository.findAll();
    }

    @GetMapping("/disciplina/{id}")
    public Disciplina retornarDisciplina(@PathVariable Long id) {
        Optional<Disciplina> disciplina = repository.findById(id);

        if (disciplina.isPresent())
            return disciplina.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Nenhuma disciplina foi encontrada com o ID informado.");
    }

    @PostMapping("/disciplina")
    @ResponseStatus(HttpStatus.CREATED)
    public Disciplina salvarDisciplina(@RequestBody @Valid Disciplina disciplina, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {

            if (disciplina.getId() == null)
                return repository.save(disciplina);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar uma nova disciplina.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novas disciplinas.");
    }

    @PutMapping("/disciplina")
    public Disciplina atualizarDisciplina(@RequestBody @Valid Disciplina disciplina,
            @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {

            if (disciplina.getId() != null)
                return repository.save(disciplina);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de disciplina para realizar alterações.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de disciplinas.");
    }

    @DeleteMapping("/disciplina/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarDisciplina(@PathVariable("id") Long itemId, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId))
            repository.deleteById(itemId);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir disciplinas.");
    }

    @GetMapping("/disciplinas/{serie}")
    public List<Disciplina> listarDisciplinasPorSerie(@PathVariable String serie) {
        return repository.findAllBySerie(serie);
    }
}