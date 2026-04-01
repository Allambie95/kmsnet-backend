package com.example.backend.BackEnd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.BackEnd.model.Inventario;
import com.example.backend.BackEnd.repository.InventarioRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // Obtener todos los productos
    public List<Inventario> getAllProductos() {
        return inventarioRepository.findAll();
    }

    // Guardar producto (crear o actualizar)
    public Inventario saveProducto(Inventario producto) {
        return inventarioRepository.save(producto);
    }

    // Buscar producto por id
    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    // Eliminar producto por id
    public void deleteProducto(Long id) {
        inventarioRepository.deleteById(id);
    }

    // Buscar producto por nombre (opcional)
    public Inventario findByNombreProducto(String nombreProducto) {
        return inventarioRepository.findByNombreProducto(nombreProducto);
    }
}
