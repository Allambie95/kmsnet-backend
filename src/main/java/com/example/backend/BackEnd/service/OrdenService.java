package com.example.backend.BackEnd.service;

import com.example.backend.BackEnd.model.*;
import com.example.backend.BackEnd.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrdenRepository ordenRepository;
    private final OrdenItemRepository ordenItemRepository;

    @Transactional
    public Orden crearOrdenDesdeCarrito(Long usuarioId) {

        // 1) Validar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe: " + usuarioId));

        // 2) Traer carrito
        List<Carrito> itemsCarrito = carritoRepository.findByUsuarioId(usuarioId);

        if (itemsCarrito.isEmpty()) {
            throw new RuntimeException("Carrito vacío para usuario: " + usuarioId);
        }

        // 3) Crear orden
        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setEstado(Orden.EstadoOrden.PENDING_PAYMENT);
        orden.setTotal(BigDecimal.ZERO);

        orden = ordenRepository.save(orden);

        // 4) Crear items y calcular total
        BigDecimal total = BigDecimal.ZERO;

        for (Carrito c : itemsCarrito) {

            Inventario producto = c.getProducto();

            BigDecimal precioUnitario =
                    BigDecimal.valueOf(producto.getPrecio());

            int cantidad = c.getCantidad();

            BigDecimal subtotal =
                    precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            OrdenItem item = new OrdenItem();
            item.setOrden(orden);
            item.setProducto(producto);
            item.setPrecioUnitario(precioUnitario);
            item.setCantidad(cantidad);
            item.setSubtotal(subtotal);

            ordenItemRepository.save(item);

            total = total.add(subtotal);
        }

        // 5) Actualizar total
        orden.setTotal(total);
        return ordenRepository.save(orden);
    }
}
