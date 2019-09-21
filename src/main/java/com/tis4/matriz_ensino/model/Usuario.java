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

@Entity @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  
    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String nomeCompleto;

    @NotBlank
    private String senha;

    @NotBlank
    private String tipo;

    public Usuario(String username, String nomeCompleto, String senha, String tipo) {
        this.username = username;
        this.nomeCompleto = nomeCompleto;
        this.senha = senha;
        this.tipo = tipo;
    }

}