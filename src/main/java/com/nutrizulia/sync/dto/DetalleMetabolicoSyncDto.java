package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad DetalleMetabolico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMetabolicoSyncDto {
    
    private UUID id;
    private UUID consultaId;
    private Integer glicemiaBasal;
    private Integer glicemaPostprandial;
    private Integer glicemiaAleatoria;
    private BigDecimal hemoglobinaGlicosilada;
    private Integer trigliceridos;
    private Integer colesterolTotal;
    private Integer colesterolHdl;
    private Integer colesterolLdl;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}