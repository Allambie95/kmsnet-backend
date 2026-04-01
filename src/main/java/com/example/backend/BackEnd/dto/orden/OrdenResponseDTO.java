package com.example.backend.BackEnd.dto.orden;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenResponseDTO {
    private Long ordenId;
    private Long usuarioId;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<OrdenItemDTO> items;
}
