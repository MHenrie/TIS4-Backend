package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.service.SecurityService;
import com.tis4.matriz_ensino.model.Turma;
import com.tis4.matriz_ensino.repository.TurmaRepository;

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
public class TurmaController {

    @Autowired
    private TurmaRepository repository;
    @Autowired
    private SecurityService security;

    @GetMapping("/turmas")
    public List<Turma> listarTurmas() {
        return repository.findAll();
    }

    @GetMapping("/turma")
    public Turma retornarTurma(@RequestParam("id") Long id) {
        Optional<Turma> turma = repository.findById(id);

        if (!turma.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma turma foi encontrada com o ID informado");

        return turma.get();
    }

    @PostMapping("/turma")
    @ResponseStatus(HttpStatus.CREATED)
    public Turma salvarTurma(@RequestBody @Valid Turma turma, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm)) {

            if (turma.getId() == null)
                return repository.save(turma);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar uma nova turma");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novas turmas");
    }

    @PutMapping("/turma")
    public Turma atualizarTurma(@RequestBody @Valid Turma turma, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm)) {

            if (turma.getId() != null) {
                Optional<Turma> turmaAtual = repository.findById(turma.getId());

                if (turmaAtual.isPresent())
                    return repository.save(turma);

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nenhuma turma foi encontrada com o ID informado");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de turma para realizar alterações");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de turmas");
    }

    @DeleteMapping("/turma")
    public void deletarTurma(@RequestParam("id") Long id, @RequestParam("adm") Long adm) {

        if (security.permissaoAdmin(adm))
            repository.deleteById(id);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir esta turma");
    }
}