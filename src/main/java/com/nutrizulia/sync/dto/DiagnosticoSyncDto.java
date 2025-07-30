package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad Diagnostico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoSyncDto {
    
    private UUID id;
    private UUID consultaId;
    private Integer riesgoBiologicoId;
    private Integer enfermedadId;
    private Boolean isPrincipal;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}