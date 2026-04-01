package com.example.backend.BackEnd.controller;

import com.example.backend.BackEnd.model.Carrito;
import com.example.backend.BackEnd.service.CarritoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Obtener todos los ítems del carrito de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Carrito> getCarritoUsuario(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarritoUsuario(usuarioId);
    }

    // Agregar producto al carrito
    // Ejemplo de llamada:
    // POST http://localhost:8015/api/carrito/add?usuarioId=1&productoId=2&cantidad=1
    @PostMapping("/add")
    public Carrito agregarProducto(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        return carritoService.agregarProducto(usuarioId, productoId, cantidad);
    }

    // Actualizar cantidad de un ítem del carrito
    // PUT http://localhost:8015/api/carrito/cantidad/5?cantidad=3
    @PutMapping("/cantidad/{itemId}")
    public Carrito actualizarCantidad(
            @PathVariable Long itemId,
            @RequestParam Integer cantidad) {

        return carritoService.actualizarCantidad(itemId, cantidad);
    }

    // Eliminar un ítem del carrito
    // DELETE http://localhost:8015/api/carrito/item/5
    @DeleteMapping("/item/{itemId}")
    public void eliminarItem(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
    }

    // Vaciar todo el carrito de un usuario
    // DELETE http://localhost:8015/api/carrito/vaciar/1
    @DeleteMapping("/vaciar/{usuarioId}")
    public void vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
    }
}
