package com.tis4.matriz_ensino.models;

public class FormLogin {

    private String username;
    private String senha;

    public FormLogin() {

    }

    public FormLogin(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSenha() {
        return this.senha;
    }

}