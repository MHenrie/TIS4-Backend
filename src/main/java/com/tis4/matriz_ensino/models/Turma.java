package com.tis4.matriz_ensino.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;
    private Long supervisorId;
    private Long professorId;

    public Turma() {

    }

    public Turma(String nome, long supervisorId, long professorId) {
        this.nome = nome;
        this.supervisorId = supervisorId;
        this.professorId = professorId;
    }

    public long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public long getSupervisorId() {
        return this.supervisorId;
    }

    public long getProfessorId() {
        return this.professorId;
    }

}