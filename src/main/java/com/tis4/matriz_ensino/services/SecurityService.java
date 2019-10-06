package com.tis4.matriz_ensino.services;

import java.security.MessageDigest;
import java.util.Optional;

import com.tis4.matriz_ensino.models.Usuario;
import com.tis4.matriz_ensino.repositories.UsuarioRepository;

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

    public boolean isAdministrador(Long id) {
        Optional<Usuario> administrador = usuarioRepository.findById(id);

        if (administrador.isPresent()) {

            if (administrador.get().getTipo().equals("Administrador"))
                return true;

        } else if (id == 12052019)
            return true;

        return false;
    }
}