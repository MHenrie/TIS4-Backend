package com.tis4.matriz_ensino.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.services.DataIntegrityService;
import com.tis4.matriz_ensino.services.SecurityService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tis4.matriz_ensino.models.Turma;
import com.tis4.matriz_ensino.repositories.TurmaRepository;

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
public class TurmaController {

    @Autowired
    private TurmaRepository repository;
    @Autowired
    private SecurityService security;
    @Autowired
    private DataIntegrityService dataIntegrity;

    @GetMapping("/turmas")
    public List<Turma> listarTurmas() {
        return repository.findAll();
    }

    @GetMapping("/turma/{id}")
    public Turma retornarTurma(@PathVariable Long id) {
        Optional<Turma> turma = repository.findById(id);

        if (turma.isPresent())
            return turma.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma turma foi encontrada com o ID informado.");
    }

    @PostMapping("/turma")
    @ResponseStatus(HttpStatus.CREATED)
    public Turma salvarTurma(@RequestBody @Valid Turma turma, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {

            if (turma.getId() == null) {

                if (!dataIntegrity.turmaExistente(turma.getNome(), turma.getAno())) {

                    Turma saved = repository.save(turma);
                    dataIntegrity.novaTurmaCascade(saved.getId(), saved.getSerie());
                    return saved;
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Já existe uma turma com este nome para o ano de " + turma.getAno() + ".");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar uma nova turma.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novas turmas.");
    }

    @PutMapping("/turma")
    public Turma atualizarTurma(@RequestBody @Valid Turma turma, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {

            if (turma.getId() != null)
                return repository.save(turma);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de turma para realizar alterações.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de turmas.");
    }

    @DeleteMapping("/turma/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarTurma(@PathVariable("id") Long turmaId, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {
            repository.deleteById(turmaId);
            dataIntegrity.excluirTurmaCascade(turmaId);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir turmas.");
    }

    @GetMapping("/turma/professor/{id}")
    public Turma retornaTurmaPorProfessor(@PathVariable("id") Long professorId, @RequestParam Short ano) {
        Optional<Turma> turma = repository.findByProfessorIdAndAno(professorId, ano);

        if (turma.isPresent())
            return turma.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há turma associada a este perfil de Professor.");
    }

    @GetMapping("/turmas/supervisor/{id}/progresso")
    public List<ObjectNode> retornaTurmasPorSupervisor(@PathVariable("id") Long supervisorId, @RequestParam Short ano) {
        List<Turma> turmas = repository.findAllBySupervisorIdAndAno(supervisorId, ano);
        List<ObjectNode> turmasProgresso = new ArrayList<ObjectNode>();

        if (turmas.size() > 0) {
            turmas.forEach(turma -> {
                turmasProgresso.add(dataIntegrity.turmaProgresso(turma));
            });
            return turmasProgresso;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Não há turmas associadas a este perfil de Supervisor.");
    }

}