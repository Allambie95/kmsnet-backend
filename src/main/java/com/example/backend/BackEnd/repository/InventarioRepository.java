package com.example.backend.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.BackEnd.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Métodos opcionales que pueden servir después
    Inventario findByNombreProducto(String nombreProducto);
}
