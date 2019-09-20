package com.tis4.matriz_ensino.service;

import java.security.MessageDigest;
import java.util.Optional;

import com.tis4.matriz_ensino.model.Usuario;
import com.tis4.matriz_ensino.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String crypto(String original) {

        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(original.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest)
                hexString.append(String.format("%02X", 0xFF & b));

            return hexString.toString();

        } catch (Exception e) {
            return original;
        }
    }

    public boolean permissaoAdmin(String username, String senha) {
        Optional<Usuario> administrador = usuarioRepository.findByUsername(username);

        if (administrador.isPresent()) {
            if (administrador.get().getTipo().equals("Administrador")) {
                if (administrador.get().getSenha().equals(this.crypto(senha)))
                    return true;
            }
        }
        return false;
    }
}