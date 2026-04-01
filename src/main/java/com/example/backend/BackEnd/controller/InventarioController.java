package com.example.backend.BackEnd.controller;

import com.example.backend.BackEnd.model.Inventario;
import com.example.backend.BackEnd.repository.InventarioRepository;
import com.example.backend.BackEnd.service.InventarioService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:5173")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los productos
    @GetMapping("/all")
    public List<Inventario> getAllProductos() {
        return inventarioService.getAllProductos();
    }

    // Crear producto
    @PostMapping("/save")
    public Inventario postProducto(@RequestBody Inventario producto) {
        return inventarioService.saveProducto(producto);
    }

    // Buscar producto por ID
    @GetMapping("/find/{id}")
    public Optional<Inventario> getProductoById(@PathVariable Long id) {
        return inventarioService.findById(id);
    }

    // Eliminar producto
    @DeleteMapping("/delete/{id}")
    public void deleteProducto(@PathVariable Long id) {
        inventarioService.deleteProducto(id);
    }


    // ------- UPDATE --------

    @Autowired
    private InventarioRepository inventarioRepository;

    @PutMapping("/update/{id}")
    public Optional<Object> putProducto(@PathVariable Long id, @RequestBody Inventario data) {

        return inventarioRepository.findById(id)
            .map(existeProducto -> {
                existeProducto.setNombreProducto(data.getNombreProducto());
                existeProducto.setPrecio(data.getPrecio());
                existeProducto.setImagenUrl(data.getImagenUrl());
                existeProducto.setDescripcion(data.getDescripcion());

                Inventario actualizado = inventarioRepository.save(existeProducto);
                return actualizado;
            });
    }
}
