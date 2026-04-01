package com.example.backend.BackEnd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario dueño del carrito
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Producto agregado al carrito
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Inventario producto;

    // Cantidad de ese producto
    private Integer cantidad;

    // Cuándo se agregó al carrito
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
