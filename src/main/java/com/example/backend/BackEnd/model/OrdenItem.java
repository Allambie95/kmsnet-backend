package com.example.backend.BackEnd.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orden_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos items pertenecen a una orden
    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_id")
    private Orden orden;

    // Guardamos referencia al producto, pero OJO:
    // también congelamos título y precio, por si cambian después.
    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Inventario producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;
}
