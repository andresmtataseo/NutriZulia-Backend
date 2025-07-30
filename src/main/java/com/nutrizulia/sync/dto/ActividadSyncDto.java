package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad Actividad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadSyncDto {

    private UUID id;
    private Integer usuarioInstitucionId;
    private Integer tipoActividadId;
    private LocalDate fecha;
    private String direccion;
    private String descripcionGeneral;
    private Integer cantidadParticipantes;
    private Integer cantidadSesiones;
    private Integer duracionMinutos;
    private String temaPrincipal;
    private String programasImplementados;
    private String urlEvidencia;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}