package com.tis4.matriz_ensino.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.models.ItemDisciplina;
import com.tis4.matriz_ensino.repositories.ItemDisciplinaRepository;
import com.tis4.matriz_ensino.services.DataIntegrityService;
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
public class ItemDisciplinaController {

    @Autowired
    private ItemDisciplinaRepository repository;
    @Autowired
    private SecurityService security;
    @Autowired
    private DataIntegrityService dataIntegrity;

    @GetMapping("/item-disciplina/{id}")
    public ItemDisciplina retornarItemDisciplina(@PathVariable Long id) {
        Optional<ItemDisciplina> itemDisciplina = repository.findById(id);

        if (itemDisciplina.isPresent())
            return itemDisciplina.get();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum item foi encontrado com o ID informado.");
    }

    @PostMapping("/item-disciplina")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDisciplina salvarItemDisciplina(@RequestBody @Valid ItemDisciplina itemDisciplina,
            @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {

            if (itemDisciplina.getId() == null) {
                ItemDisciplina saved = repository.save(itemDisciplina);
                dataIntegrity.novoItemDisciplinaCascade(saved.getId(), saved.getDisciplinaId());
                return saved;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é admitido um valor de ID para criar um novo item.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novos itens.");
    }

    @PutMapping("/item-disciplina")
    public ItemDisciplina atualizarItemDisciplina(@RequestBody @Valid ItemDisciplina itemDisciplina,
            @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId) && itemDisciplina.isGlobal()
                || security.isProfessor(userId) && !itemDisciplina.isGlobal()) {

            if (itemDisciplina.getId() != null)
                return repository.save(itemDisciplina);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "É necessário informar um ID de item para realizar alterações.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para alterar os dados de itens.");
    }

    @DeleteMapping("/item-disciplina/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarItemDisciplina(@PathVariable("id") Long itemId, @RequestParam("user") Long userId) {

        if (security.isAdministrador(userId)) {
            repository.deleteById(itemId);
            dataIntegrity.excluirItemDisciplinaCascade(itemId);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir itens.");
    }

    @GetMapping("/disciplina/{id}/itens-disciplina")
    public List<ItemDisciplina> listarItensPorDisciplina(@PathVariable("id") Long disciplinaId) {
        return repository.findAllByDisciplinaIdAndGlobalTrue(disciplinaId);
    }

}