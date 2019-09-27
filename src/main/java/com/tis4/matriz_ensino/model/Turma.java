package com.tis4.matriz_ensino.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Turma {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter @NotBlank
    private String nome;

    @Setter @NotBlank
    private String serie;

    @Setter @NotNull
    private Integer ano;
  
    @Setter
    private Long supervisorId;
  
    @Setter
    private Long professorId;

}