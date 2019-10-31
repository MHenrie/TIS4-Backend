package com.tis4.matriz_ensino.models;

public class Item {

    private Long id;
    private Long itemDisciplinaId;
    private String titulo;
    private String descricao;
    private String objetivo;
    private Long disciplinaId;
    private Byte bimestre;
    private Boolean global;
    private Long turmaId;
    private String status;

    public Item(ItemTurma itemTurma, ItemDisciplina itemDisciplina) {
        this.id = itemTurma.getId();
        this.itemDisciplinaId = itemDisciplina.getId();
        this.titulo = itemDisciplina.getTitulo();
        this.descricao = itemDisciplina.getDescricao();
        this.objetivo = itemDisciplina.getObjetivo();
        this.disciplinaId = itemDisciplina.getDisciplinaId();
        this.bimestre = itemDisciplina.getBimestre();
        this.global = itemDisciplina.getGlobal();
        this.turmaId = itemTurma.getTurmaId();
        this.status = itemTurma.getStatus();
    }

    public Item() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemDisciplinaId(Long itemDisciplinaId) {
        this.itemDisciplinaId = itemDisciplinaId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setBimestre(Byte bimestre) {
        this.bimestre = bimestre;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ItemDisciplina getItemDisciplina() {
        return new ItemDisciplina(this.itemDisciplinaId, this.titulo, this.descricao, this.objetivo, this.disciplinaId,
                this.bimestre, this.global);
    }

    public ItemTurma getItemTurma() {
        return new ItemTurma(this.id, this.itemDisciplinaId, this.turmaId, this.status);
    }

}