package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para las respuestas de sincronización (pull) del servidor al cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncPullResponseDto {
    
    /**
     * ID único de la respuesta de sincronización
     */
    private UUID syncResponseId;
    
    /**
     * ID del usuario institución
     */
    private Integer usuarioInstitucionId;
    
    /**
     * Timestamp del servidor al momento de la sincronización
     */
    private LocalDateTime serverTimestamp;
    
    /**
     * Timestamp desde el cual se obtuvieron los cambios
     */
    private LocalDateTime fromTimestamp;
    
    /**
     * Indica si hay más datos disponibles para sincronizar
     */
    private boolean hasMoreData;
    
    /**
     * Número total de registros disponibles
     */
    private long totalRecords;
    
    /**
     * Lista de pacientes modificados desde la última sincronización
     */
    private List<PacienteSyncDto> pacientes;
    
    /**
     * Lista de representantes modificados desde la última sincronización
     */
    private List<RepresentanteSyncDto> representantes;
    
    /**
     * Lista de consultas modificadas desde la última sincronización
     */
    private List<ConsultaSyncDto> consultas;
    
    /**
     * Lista de actividades modificadas desde la última sincronización
     */
    private List<ActividadSyncDto> actividades;
    
    /**
     * Lista de relaciones paciente-representante modificadas
     */
    private List<PacienteRepresentanteSyncDto> pacientesRepresentantes;
    
    /**
     * Lista de detalles antropométricos modificados
     */
    private List<DetalleAntropometricoSyncDto> detallesAntropometricos;
    
    /**
     * Lista de detalles vitales modificados
     */
    private List<DetalleVitalSyncDto> detallesVitales;
    
    /**
     * Lista de detalles metabólicos modificados
     */
    private List<DetalleMetabolicoSyncDto> detallesMetabolicos;
    
    /**
     * Lista de detalles de obstetricia modificados
     */
    private List<DetalleObstetriciaSyncDto> detallesObstetricia;
    
    /**
     * Lista de detalles pediátricos modificados
     */
    private List<DetallePedriatricoSyncDto> detallesPedriatricos;
    
    /**
     * Lista de evaluaciones antropométricas modificadas
     */
    private List<EvaluacionAntropometricaSyncDto> evaluacionesAntropometricas;
    
    /**
     * Lista de diagnósticos modificados
     */
    private List<DiagnosticoSyncDto> diagnosticos;
}