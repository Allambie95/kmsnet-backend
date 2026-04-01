package com.example.backend.BackEnd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.BackEnd.model.Carrito;
import com.example.backend.BackEnd.model.Inventario;
import com.example.backend.BackEnd.model.Usuario;
import com.example.backend.BackEnd.repository.CarritoRepository;
import com.example.backend.BackEnd.repository.InventarioRepository;
import com.example.backend.BackEnd.repository.UsuarioRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    // Obtener todo el carrito de un usuario por ID
    public List<Carrito> obtenerCarritoUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    // Agregar un producto al carrito
    public Carrito agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Inventario producto = inventarioRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Si ya existe el ítem en el carrito, se incrementa cantidad
        List<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        for (Carrito item : carrito) {
            if (item.getProducto().getId().equals(productoId)) {
                item.setCantidad(item.getCantidad() + cantidad);
                return carritoRepository.save(item);
            }
        }

        // Si es nuevo, se crea el ítem
        Carrito nuevoItem = new Carrito();
        nuevoItem.setUsuario(usuario);
        nuevoItem.setProducto(producto);
        nuevoItem.setCantidad(cantidad);

        return carritoRepository.save(nuevoItem);
    }

    // Eliminar un ítem del carrito
    public void eliminarItem(Long itemId) {
        carritoRepository.deleteById(itemId);
    }

    // Vaciar carrito completo
    public void vaciarCarrito(Long usuarioId) {
        List<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        carritoRepository.deleteAll(carrito);
    }

    // Actualizar cantidad de un ítem
    public Carrito actualizarCantidad(Long itemId, Integer nuevaCantidad) {
        Optional<Carrito> itemOpt = carritoRepository.findById(itemId);

        if (itemOpt.isPresent()) {
            Carrito item = itemOpt.get();
            item.setCantidad(nuevaCantidad);
            return carritoRepository.save(item);
        }

        throw new RuntimeException("Item del carrito no encontrado");
    }
}
