package com.tis4.matriz_ensino.controller;

import com.tis4.matriz_ensino.repository.UsuarioRepository;
import com.tis4.matriz_ensino.service.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ItemDisciplinaController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private SecurityService security;

}