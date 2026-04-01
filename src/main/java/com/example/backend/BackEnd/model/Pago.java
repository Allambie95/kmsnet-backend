package com.example.backend.BackEnd.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Un pago pertenece a una orden
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Proveedor proveedor; // MERCADOPAGO, WEBPAY

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado; // INITIATED, PENDING, APPROVED...

    // ID externo del provider (cuando exista)
    @Column(name = "provider_payment_id")
    private String providerPaymentId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Opcional: para auditoría (texto JSON)
    @Lob
    @Column(name = "raw_payload")
    private String rawPayload;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) this.estado = EstadoPago.INITIATED;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public enum Proveedor {
        MERCADOPAGO,
        WEBPAY
    }

    public enum EstadoPago {
        INITIATED,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED
    }
}
