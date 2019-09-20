package com.tis4.matriz_ensino.model.accessory;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManipularUsuario {

    private Long id;
    @NotBlank
    private String nomeCompleto;
    @NotBlank
    private String username;
   
    private String senha;
    @NotBlank
    private String tipo;

    @NotBlank
    private String usernameAdm;
    @NotBlank
    private String senhaAdm;
   
}