package com.tis4.matriz_ensino.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tis4.matriz_ensino.models.Item;
import com.tis4.matriz_ensino.models.ItemDisciplina;
import com.tis4.matriz_ensino.models.ItemTurma;
import com.tis4.matriz_ensino.repositories.ItemDisciplinaRepository;
import com.tis4.matriz_ensino.repositories.ItemTurmaRepository;
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
public class ItemTurmaController {

    @Autowired
    private ItemTurmaRepository iTurmaRepository;
    @Autowired
    private ItemDisciplinaRepository iDisciplinaRepository;
    @Autowired
    private SecurityService security;
    @Autowired
    private DataIntegrityService dataIntegrity;

    @GetMapping("/item-turma/{id}")
    public ObjectNode retornarItemTurmaCompleto(@PathVariable Long id) {
        Optional<ItemTurma> itemTurma = iTurmaRepository.findById(id);

        if (itemTurma.isPresent()) {
            Long itemDisciplinaId = itemTurma.get().getItemDisciplinaId();
            Optional<ItemDisciplina> itemDisciplina = iDisciplinaRepository.findById(itemDisciplinaId);

            return dataIntegrity.itemComplete(itemTurma.get(), itemDisciplina.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum item foi encontrado com o ID informado.");
    }

    @GetMapping("/item-turma/{id}/descricao")
    public String retornarItemTurmaDescricao(@PathVariable Long id) {
        Optional<ItemTurma> itemTurma = iTurmaRepository.findById(id);

        if (itemTurma.isPresent()) {
            Long itemDisciplinaId = itemTurma.get().getItemDisciplinaId();
            Optional<ItemDisciplina> itemDisciplina = iDisciplinaRepository.findById(itemDisciplinaId);

            return itemDisciplina.get().getDescricao();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum item foi encontrado com o ID informado.");
    }

    @PostMapping("/item-turma")
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectNode salvarItemTurma(@RequestBody Item item, @RequestParam("user") Long userId) {

        if (security.isProfessor(userId)) {

            ItemDisciplina itemDisciplina;
            ItemTurma itemTurma;

            if (item.getItemDisciplina().getId() == null && item.getItemTurma().getId() == null) {

                itemDisciplina = iDisciplinaRepository.save(item.getItemDisciplina());
                item.setItemDisciplinaId(itemDisciplina.getId());
                itemTurma = iTurmaRepository.save(item.getItemTurma());
                return dataIntegrity.itemResume(itemTurma, itemDisciplina);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não são admitidos valores de ID para criar um item");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha de Autenticação: você não tem permissão para criar novos itens.");
    }

    @PutMapping("/item-turma/{id}")
    public void atualizarStatusItemTurma(@RequestBody String status, @PathVariable("id") Long itemId,
            @RequestParam("user") Long userId) {

        if (security.isProfessor(userId)) {
            Optional<ItemTurma> itemTurma = iTurmaRepository.findById(itemId);
            itemTurma.get().setStatus(status);
            iTurmaRepository.save(itemTurma.get());
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para alterar os dados deste item.");
    }

    @DeleteMapping("/item-turma/{id}")
    public void deletarItemTurma(@PathVariable("id") Long itemId, @RequestParam("user") Long userId) {

        if (security.isProfessor(userId)) {
            Optional<ItemTurma> itemTurma = iTurmaRepository.findById(itemId);
            iTurmaRepository.deleteById(itemId);
            iDisciplinaRepository.deleteById(itemTurma.get().getItemDisciplinaId());
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falha de Autenticação: você não tem permissão para excluir este item.");
    }

    @GetMapping("/itens-turma/turma/{turmaId}/disciplina/{disciplinaId}")
    public List<ObjectNode> listarItensPorTurmaAndDisciplina(@PathVariable Long turmaId, @PathVariable Long disciplinaId) {

        List<ItemTurma> itensTurma = iTurmaRepository.findAllByTurmaId(turmaId);
        List<ObjectNode> lista = new ArrayList<ObjectNode>();

        itensTurma.forEach(item -> {
            ItemDisciplina itemDisciplina = iDisciplinaRepository.findById(item.getItemDisciplinaId()).get();
            if (itemDisciplina.getDisciplinaId() == disciplinaId)
                lista.add(dataIntegrity.itemResume(item, itemDisciplina));
        });

        return lista;
    }
    

    @GetMapping("/itens-turma/turma/{turmaId}/disciplina/{disciplinaId}/quantidade")
    public List<Integer> retornarQuantidadePorDisciplina(@PathVariable Long turmaId, @PathVariable Long disciplinaId,
            @RequestParam String status) {
        List<ItemTurma> itensTurma = iTurmaRepository.findAllByTurmaId(turmaId);
        Integer total = 0, counter = 0;

        for (ItemTurma item : itensTurma) {
            ItemDisciplina itemDisciplina = iDisciplinaRepository.findById(item.getItemDisciplinaId()).get();
            if (itemDisciplina.getDisciplinaId() == disciplinaId) {
                total++;
                if (item.getStatus().equals(status))
                    counter++;
            }
        }
        return Arrays.asList(counter, total);
    }

}