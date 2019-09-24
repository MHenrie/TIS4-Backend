package com.tis4.matriz_ensino.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  
    @NotBlank @Setter
    @Column(unique = true)
    private String username;

    @NotBlank @Setter
    private String nomeCompleto;

    @NotBlank @Setter
    private String senha;

    @NotBlank @Setter
    private String tipo;

}