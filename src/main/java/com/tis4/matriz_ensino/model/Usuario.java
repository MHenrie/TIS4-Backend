package com.tis4.matriz_ensino.model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tis4.matriz_ensino.implement.Security;

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

    @Setter
    @NotBlank @Column(unique = true)
    private String username;

    @Setter
    @NotBlank
    private String nomeCompleto;

    @NotBlank
    private String senha;

    @Setter
    @NotBlank
    private String tipo;

    public void setSenha(String original) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.senha = Security.crypto(original);
    }
}