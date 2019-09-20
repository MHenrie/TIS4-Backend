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
public class ManipularTurma {

    private Long id;

    @NotBlank
    private String nome;
    
    private String supervisor;
    @NotBlank
    private String professor;

    @NotBlank
    private String usernameAdm;
    @NotBlank
    private String senhaAdm;

    
}