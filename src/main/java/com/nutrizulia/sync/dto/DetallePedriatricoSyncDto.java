package com.nutrizulia.sync.dto;

import com.nutrizulia.common.enums.TipoLactancia;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad DetallePedriatrico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedriatricoSyncDto {
    
    private UUID id;
    private UUID consultaId;
    private Boolean usaBiberon;
    private TipoLactancia tipoLactancia;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}