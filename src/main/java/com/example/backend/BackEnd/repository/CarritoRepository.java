package com.example.backend.BackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.BackEnd.model.Carrito;
import com.example.backend.BackEnd.model.Usuario;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Todos los ítems de carrito de un usuario
    List<Carrito> findByUsuario(Usuario usuario);

    // Alternativa por id de usuario
    List<Carrito> findByUsuarioId(Long usuarioId);

    // 🔥 NUEVO: borrar carrito completo del usuario
    void deleteByUsuarioId(Long usuarioId);
}