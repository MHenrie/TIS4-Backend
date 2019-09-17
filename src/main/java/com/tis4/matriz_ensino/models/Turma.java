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
    private String supervisor;
    private String professor;

    public Turma() {

    }

    public Turma(String nome, String supervisor, String professor) {
        this.nome = nome;
        this.supervisor = supervisor;
        this.professor = professor;
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getSupervisor() {
        return this.supervisor;
    }

    public String getProfessor() {
        return this.professor;
    }

}