package com.example.backend.BackEnd.controller;

import com.example.backend.BackEnd.dto.orden.OrdenItemDTO;
import com.example.backend.BackEnd.dto.orden.OrdenResponseDTO;
import com.example.backend.BackEnd.model.Orden;
import com.example.backend.BackEnd.model.OrdenItem;
import com.example.backend.BackEnd.repository.OrdenItemRepository;
import com.example.backend.BackEnd.repository.OrdenRepository;
import com.example.backend.BackEnd.service.OrdenService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://kmsnet.cl",
    "https://www.kmsnet.cl"
})
public class OrdenController {

    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;
    private final OrdenItemRepository ordenItemRepository;

    @PostMapping("/crear-desde-carrito/{usuarioId}")
    public ResponseEntity<OrdenResponseDTO> crearDesdeCarrito(@PathVariable Long usuarioId) {
        Orden orden = ordenService.crearOrdenDesdeCarrito(usuarioId);
        return ResponseEntity.ok(toResponseDTO(orden));
    }

    @GetMapping("/{ordenId}")
    public ResponseEntity<OrdenResponseDTO> obtenerOrden(@PathVariable Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no existe: " + ordenId));

        return ResponseEntity.ok(toResponseDTO(orden));
    }

    private OrdenResponseDTO toResponseDTO(Orden orden) {

        List<OrdenItem> items = ordenItemRepository.findByOrdenId(orden.getId());

        List<OrdenItemDTO> itemsDTO = items.stream()
                .map(oi -> OrdenItemDTO.builder()
                        .productoId(oi.getProducto().getId())
                        .nombreProducto(oi.getProducto().getNombreProducto())
                        .imagenUrl(oi.getProducto().getImagenUrl())
                        .precioUnitario(oi.getPrecioUnitario())
                        .cantidad(oi.getCantidad())
                        .subtotal(oi.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrdenResponseDTO.builder()
                .ordenId(orden.getId())
                .usuarioId(orden.getUsuario().getId())
                .total(orden.getTotal())
                .estado(orden.getEstado().name())
                .fechaCreacion(orden.getFechaCreacion())
                .items(itemsDTO)
                .build();
    }
}