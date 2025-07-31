package com.nutrizulia.collection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad PacienteRepresentante
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRepresentanteDto {
    
    private UUID id;
    private Integer usuarioInstitucionId;
    private UUID pacienteId;
    private UUID representanteId;
    private Integer parentescoId;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}