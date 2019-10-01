package com.tis4.matriz_ensino.model.accessory;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class FormLogin {

    @NotBlank
    private String username;
    @NotBlank
    private String senha;

}