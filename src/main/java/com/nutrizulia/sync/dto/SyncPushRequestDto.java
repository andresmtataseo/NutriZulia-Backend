package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para las solicitudes de sincronización (push) desde el cliente al servidor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncPushRequestDto {
    
    /**
     * ID único de la solicitud de sincronización
     */
    private UUID syncRequestId;
    
    /**
     * ID del usuario institución que realiza la sincronización
     */
    private Integer usuarioInstitucionId;
    
    /**
     * Timestamp de la última sincronización del cliente
     */
    private LocalDateTime lastSyncTimestamp;
    
    /**
     * Timestamp actual del cliente
     */
    private LocalDateTime clientTimestamp;
    
    /**
     * Lista de pacientes a sincronizar
     */
    private List<PacienteSyncDto> pacientes;
    
    /**
     * Lista de representantes a sincronizar
     */
    private List<RepresentanteSyncDto> representantes;
    
    /**
     * Lista de consultas a sincronizar
     */
    private List<ConsultaSyncDto> consultas;
    
    /**
     * Lista de actividades a sincronizar
     */
    private List<ActividadSyncDto> actividades;
    
    /**
     * Lista de relaciones paciente-representante a sincronizar
     */
    private List<PacienteRepresentanteSyncDto> pacientesRepresentantes;
    
    /**
     * Lista de detalles antropométricos a sincronizar
     */
    private List<DetalleAntropometricoSyncDto> detallesAntropometricos;
    
    /**
     * Lista de detalles vitales a sincronizar
     */
    private List<DetalleVitalSyncDto> detallesVitales;
    
    /**
     * Lista de detalles metabólicos a sincronizar
     */
    private List<DetalleMetabolicoSyncDto> detallesMetabolicos;
    
    /**
     * Lista de detalles de obstetricia a sincronizar
     */
    private List<DetalleObstetriciaSyncDto> detallesObstetricia;
    
    /**
     * Lista de detalles pediátricos a sincronizar
     */
    private List<DetallePedriatricoSyncDto> detallesPedriatricos;
    
    /**
     * Lista de evaluaciones antropométricas a sincronizar
     */
    private List<EvaluacionAntropometricaSyncDto> evaluacionesAntropometricas;
    
    /**
     * Lista de diagnósticos a sincronizar
     */
    private List<DiagnosticoSyncDto> diagnosticos;
}