package com.nutrizulia.collection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad DetalleObstetricia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleObstetriciaDto {
    
    private UUID id;
    private UUID consultaId;
    private Boolean estaEmbarazada;
    private LocalDate fechaUltimaMenstruacion;
    private Integer semanasGestacion;
    private BigDecimal pesoPreEmbarazo;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}