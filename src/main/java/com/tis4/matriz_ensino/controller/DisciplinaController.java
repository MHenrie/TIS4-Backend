package com.tis4.matriz_ensino.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tis4.matriz_ensino.model.Disciplina;
import com.tis4.matriz_ensino.repository.DisciplinaRepository;
import com.tis4.matriz_ensino.service.SecurityService;

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
@RequestMapping("/api/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repository;
    @Autowired
    private SecurityService security;

    @GetMapping
    public List<Disciplina> findAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Disciplina findById(@PathVariable long id){
        Optional<Disciplina> oDisciplina = repository.findById(id);
        
        if(!oDisciplina.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada");

        return oDisciplina.get();
    }
    
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Disciplina register(@RequestBody @Valid Disciplina disciplina, @RequestParam("user") Long userId) {
        if (security.isAdministrador(userId)){
            Optional<Disciplina> oDisciplina = repository.findByNome(disciplina.getNome());
            if(oDisciplina.isPresent())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe uma disciplina com este nome");
            else
                repository.save(disciplina);
        } else 
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Você não tem permissão para criar disciplinas");

        return null;
    }
 
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Disciplina update(@RequestBody @Valid Disciplina disciplina) {
        Optional<Disciplina> oDisciplina = repository.findById(disciplina.getDisciplinaId());
        if (oDisciplina.isPresent()) { 
            repository.save(disciplina);
            return disciplina;
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        Optional<Disciplina> oDisciplina = repository.findById(id);
        if (oDisciplina.isPresent()) {
            repository.deleteById(id);
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}