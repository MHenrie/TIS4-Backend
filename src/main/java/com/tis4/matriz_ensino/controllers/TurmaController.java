package com.tis4.matriz_ensino.controllers;

import java.util.List;

import javax.validation.Valid;

import com.tis4.matriz_ensino.models.Turma;
import com.tis4.matriz_ensino.repository.TurmaRepository;

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
public class TurmaController {

    @Autowired
    private TurmaRepository repository;

    @GetMapping("/turmas")
    public List<Turma> listaTurmas() {
        return repository.findAll();
    }

    @GetMapping("/turma/{id}")
    public Turma retornaTurma(@PathVariable(value = "id") long id) {
        return repository.findById(id);
    }

    @PostMapping("/turma")
    public Turma salvaTurma(@RequestBody @Valid Turma turma) {
        return repository.save(turma);
    }

    @PutMapping("/turma")
    public Turma atualizaTurma(@RequestBody @Valid Turma turma) {
        return repository.save(turma);
    }

    @DeleteMapping("/turma")
    public void deletaTurma(@RequestBody Turma turma) {
        repository.delete(turma);
    }
}