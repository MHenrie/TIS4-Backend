package com.tis4.matriz_ensino.services;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tis4.matriz_ensino.models.Disciplina;
import com.tis4.matriz_ensino.models.ItemDisciplina;
import com.tis4.matriz_ensino.models.ItemTurma;
import com.tis4.matriz_ensino.models.Turma;
import com.tis4.matriz_ensino.models.Usuario;
import com.tis4.matriz_ensino.repositories.DisciplinaRepository;
import com.tis4.matriz_ensino.repositories.ItemDisciplinaRepository;
import com.tis4.matriz_ensino.repositories.ItemTurmaRepository;
import com.tis4.matriz_ensino.repositories.TurmaRepository;
import com.tis4.matriz_ensino.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataIntegrityService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private ItemDisciplinaRepository iDisciplinaRepository;
    @Autowired
    private ItemTurmaRepository iTurmaRepository;

    public boolean usernameDisponivel(String username) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(username);

        if (usuarioExistente.isPresent())
            return false;

        return true;
    }

    public boolean usernameEqualsId(String username, Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsernameAndId(username, id);

        return usuarioExistente.isPresent() ? true : false;
    }

    public boolean mesmaSenha(Long id, String senha) {
        Optional<Usuario> usuarioAtual = usuarioRepository.findById(id);

        return usuarioAtual.get().getSenha().equals(senha) ? true : false;
    }

    public ObjectNode itemResume(ItemTurma itemTurma, ItemDisciplina itemDisciplina) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode item = mapper.createObjectNode();

        item.put("id", itemTurma.getId());
        item.put("titulo", itemDisciplina.getTitulo());
        item.put("objetivo", itemDisciplina.getObjetivo());
        item.put("bimestre", itemDisciplina.getBimestre());
        item.put("global", itemDisciplina.getGlobal());
        item.put("status", itemTurma.getStatus());

        return item;
    }

    public ObjectNode itemComplete(ItemTurma itemTurma, ItemDisciplina itemDisciplina) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode item = mapper.createObjectNode();

        item.put("id", itemTurma.getId());
        item.put("itemDisciplinaId", itemDisciplina.getId());
        item.put("titulo", itemDisciplina.getTitulo());
        item.put("descricao", itemDisciplina.getDescricao());
        item.put("objetivo", itemDisciplina.getObjetivo());
        item.put("bimestre", itemDisciplina.getBimestre());
        item.put("global", itemDisciplina.getGlobal());
        item.put("status", itemTurma.getStatus());
        item.put("turmaId", itemTurma.getTurmaId());
        item.put("disciplinaId", itemDisciplina.getDisciplinaId());

        return item;
    }

    public void novaTurmaCascade(Long turmaId, String serie) {
        List<Disciplina> disciplinasPorSerie = disciplinaRepository.findAllBySerie(serie);
        disciplinasPorSerie.forEach(disciplina -> {
            List<ItemDisciplina> itensDisciplinas = iDisciplinaRepository
                    .findAllByDisciplinaIdAndGlobalTrue(disciplina.getId());
            itensDisciplinas.forEach(item -> iTurmaRepository.save(new ItemTurma(item.getId(), turmaId)));
        });
    }

    public void excluirTurmaCascade(Long turmaId) {
        List<ItemTurma> itensTurma = iTurmaRepository.findAllByTurmaId(turmaId);
        itensTurma.forEach(item -> iTurmaRepository.delete(item));
    }

    public void novoItemDisciplinaCascade(Long itemDisciplinaId, Long disciplinaId) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(disciplinaId);
        List<Turma> turmas = turmaRepository.findAllBySerie(disciplina.get().getSerie());
        turmas.forEach(turma -> iTurmaRepository.save(new ItemTurma(itemDisciplinaId, turma.getId())));
    }

    public void excluirItemDisciplinaCascade(Long itemDisciplinaId) {
        List<ItemTurma> itens = iTurmaRepository.findAllByItemDisciplinaId(itemDisciplinaId);
        itens.forEach(item -> iTurmaRepository.delete(item));
    }

}