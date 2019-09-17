package com.tis4.matriz_ensino.models;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tis4.matriz_ensino.Security;

@Entity
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

    public Usuario() {

    }

    public Usuario(String nomeCompleto, String username, String senha, String tipo)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        this.nomeCompleto = nomeCompleto;
        this.username = username;
        this.setSenha(senha);
        this.tipo = tipo;
    }

    public void setSenha(String original) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.senha = Security.crypto(original);
    }

    public Long getId() {
        return this.id;
    }

    public String getNomeCompleto() {
        return this.nomeCompleto;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getTipo() {
        return this.tipo;
    }
}