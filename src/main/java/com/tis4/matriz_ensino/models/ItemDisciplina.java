package com.tis4.matriz_ensino.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDisciplina {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descricao;
    @NotBlank
    private String objetivo;
    @NotNull
    private Long disciplinaId;
    @NotNull
    private Byte bimestre;
    @NotNull
    private Boolean global;

    public ItemDisciplina(Long id, String titulo, String descricao, String objetivo, Long disciplinaId, Byte bimestre, Boolean global) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.objetivo = objetivo;
        this.disciplinaId = disciplinaId;
        this.bimestre = bimestre;
        this.global = global == null ? true : false;
    }

    public ItemDisciplina(){

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObjetivo() {
        return this.objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Long getDisciplinaId() {
        return this.disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public Byte getBimestre() {
        return this.bimestre;
    }

    public void setBimestre(Byte bimestre) {
        this.bimestre = bimestre;
    }

    public Boolean getGlobal() {
        return this.global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public Boolean isGlobal() {
        return this.global;
    }

}