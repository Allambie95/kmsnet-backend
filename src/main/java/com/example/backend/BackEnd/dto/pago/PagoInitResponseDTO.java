package com.example.backend.BackEnd.dto.pago;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoInitResponseDTO {
    private Long pagoId;
    private Long ordenId;
    private String proveedor;
    private String estado;
    private BigDecimal monto;

    // Solo Webpay:
    private String redirectUrl; // URL de Webpay (formAction)
    private String token;       // token_ws
}
