package com.tis4.matriz_ensino.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.models.ItemTurma;
import com.tis4.matriz_ensino.repositories.ItemTurmaRepository;
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
public class ItemTurmaController {

    @Autowired
    private ItemTurmaRepository repository;
    @Autowired
    private SecurityService security;

    @GetMapping("/item-turma/{id}")
    public ItemTurma retornarItemTurma(@PathVariable Long id) {
        Optional<ItemTurma> itemTurma = repository.findById(id);

        if (itemTurma.isPresent())
            return itemTurma.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum item foi encontrado com o ID informado.");
    }

    @PostMapping("/item-turma")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemTurma salvarItemTurma(@RequestBody @Valid ItemTurma itemTurma, @RequestParam("user") Long userId) {

        if (security.isProfessor(userId)) {

            if (itemTurma.getId() == null)
                return repository.save(itemTurma);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar um novo item.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novos itens.");
    }

    @PutMapping("/item-turma")
    public ItemTurma atualizarItemTurma(@RequestBody @Valid ItemTurma itemTurma, @RequestParam("user") Long userId) {

        if (security.isProfessor(userId)) {

            if (itemTurma.getId() != null)
                return repository.save(itemTurma);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de item para realizar alterações.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de itens.");
    }

    @DeleteMapping("/item-turma/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarItemTurma(@PathVariable("id") Long itemId, @RequestParam("user") Long userId) {

        if (security.isProfessor(userId))
            repository.deleteById(itemId);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir itens.");
    }

    @GetMapping("disciplina/{disciplinaId}/turma/{turmaId}/itens-turma")
    public List<ItemTurma> listarItensPorTurma(@PathVariable Long disciplinaId, @PathVariable Long turmaId,
            @RequestParam("filter") Boolean hasFilter) {

        if (!hasFilter)
            return repository.findAllByDisciplinaIdAndTurmaId(disciplinaId, turmaId);
        else
            return repository.findAllByDisciplinaIdAndTurmaIdAndGlobalFalse(disciplinaId, turmaId);
    }

}