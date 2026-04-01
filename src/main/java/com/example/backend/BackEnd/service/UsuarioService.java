package com.example.backend.BackEnd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.BackEnd.model.Usuario;
import com.example.backend.BackEnd.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Guardar un usuario (crear o actualizar) con validaciones de unicidad
    public Usuario saveUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no puede ser null.");
        }

        // Normalización básica
        String nombreUsuario = usuario.getNombreUsuario() == null ? "" : usuario.getNombreUsuario().trim();
        String mail = usuario.getMail() == null ? "" : usuario.getMail().trim().toLowerCase();

        if (nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (mail.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        // Si es UPDATE (tiene id), buscamos el usuario actual para comparar
        Long id = usuario.getId();
        if (id != null) {
            Usuario actual = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no existe con id: " + id));

            // Si cambió el username, validamos duplicado
            if (!actual.getNombreUsuario().equalsIgnoreCase(nombreUsuario)
                    && usuarioRepository.existsByNombreUsuarioIgnoreCase(nombreUsuario)) {
                throw new IllegalStateException("El nombre de usuario ya está en uso.");
            }

            // Si cambió el mail, validamos duplicado
            if (!actual.getMail().equalsIgnoreCase(mail)
                    && usuarioRepository.existsByMailIgnoreCase(mail)) {
                throw new IllegalStateException("El correo ya está registrado.");
            }

        } else {
            // Si es CREATE (sin id), validamos duplicado directo
            if (usuarioRepository.existsByNombreUsuarioIgnoreCase(nombreUsuario)) {
                throw new IllegalStateException("El nombre de usuario ya está en uso.");
            }
            if (usuarioRepository.existsByMailIgnoreCase(mail)) {
                throw new IllegalStateException("El correo ya está registrado.");
            }

            // Si no viene rol, asignamos por defecto
            if (usuario.getRol() == null || usuario.getRol().isBlank()) {
                usuario.setRol("USUARIO");
            }
        }

        // Guardamos normalizado
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setMail(mail);

        return usuarioRepository.save(usuario);
    }

    // Buscar por id
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Eliminar por id
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Buscar por nombre de usuario (útil para login)
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    // Buscar por mail (útil para login/validaciones)
    public Usuario findByMail(String mail) {
        return usuarioRepository.findByMail(mail);
    }
}
