package com.example.backend.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.BackEnd.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Para login / búsquedas
    Usuario findByNombreUsuario(String nombreUsuario);
    Usuario findByMail(String mail);

    // Para validaciones de unicidad
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByMailIgnoreCase(String mail);
}
