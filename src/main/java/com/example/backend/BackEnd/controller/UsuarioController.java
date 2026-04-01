package com.example.backend.BackEnd.controller;

import com.example.backend.BackEnd.model.Usuario;
import com.example.backend.BackEnd.service.UsuarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173") // tu front
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping("/all")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Crear usuario
    @PostMapping("/save")
    public ResponseEntity<?> postUsuario(@RequestBody Usuario entity) {
        try {
            // si no viene rol desde el front, lo dejamos por defecto como USUARIO
            if (entity.getRol() == null || entity.getRol().isBlank()) {
                entity.setRol("USUARIO");
            }

            Usuario creado = usuarioService.saveUsuario(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);

        } catch (IllegalStateException ex) {
            // Duplicado username/mail
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());

        } catch (IllegalArgumentException ex) {
            // Campos obligatorios / usuario null / etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Buscar usuario por id
    @GetMapping("/find/{id}")
    public ResponseEntity<?> getUsuarioId(@PathVariable Long id) {
        return usuarioService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado con id: " + id));
    }

    // Update usuario (pasa por el Service para respetar unicidad)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> putUsuario(@PathVariable Long id, @RequestBody Usuario entity) {
        try {
            // Forzamos el id del path
            entity.setId(id);

            Usuario actualizado = usuarioService.saveUsuario(entity);
            return ResponseEntity.ok(actualizado);

        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Eliminar usuario por id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado con id: " + id);
        }
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
