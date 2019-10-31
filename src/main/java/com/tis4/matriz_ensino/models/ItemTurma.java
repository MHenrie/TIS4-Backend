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
public class ItemTurma {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long itemDisciplinaId;
    @NotNull
    private Long turmaId;
    @NotBlank
    private String status;

    public ItemTurma(Long id, Long itemDisciplinaId, Long turmaId, String status) {
        this.id = id;
        this.itemDisciplinaId = itemDisciplinaId;
        this.turmaId = turmaId;
        this.status = status == null ? "Pendente" : status;
    }

    public ItemTurma(Long itemDisciplinaId, Long turmaId) {
        this.itemDisciplinaId = itemDisciplinaId;
        this.turmaId = turmaId;
        this.status = "Pendente";
    }

    public ItemTurma() {
        
    }

	public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemDisciplinaId() {
        return this.itemDisciplinaId;
    }

    public void setItemDisciplinaId(Long itemDisciplinaId) {
        this.itemDisciplinaId = itemDisciplinaId;
    }

    public Long getTurmaId() {
        return this.turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}