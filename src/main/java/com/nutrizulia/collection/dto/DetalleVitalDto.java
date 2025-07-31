package com.nutrizulia.collection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad DetalleVital
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVitalDto {
    
    private UUID id;
    private UUID consultaId;
    private Integer tensionArterialSistolica;
    private Integer tensionArterialDiastolica;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private BigDecimal temperatura;
    private Integer saturacionOxigeno;
    private Integer pulso;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}