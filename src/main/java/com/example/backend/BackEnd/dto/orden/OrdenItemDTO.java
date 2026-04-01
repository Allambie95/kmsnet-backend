package com.example.backend.BackEnd.dto.orden;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenItemDTO {
    private Long productoId;
    private String nombreProducto;
    private String imagenUrl;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
