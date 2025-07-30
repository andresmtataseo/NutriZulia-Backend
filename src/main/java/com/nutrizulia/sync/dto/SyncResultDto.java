package com.nutrizulia.sync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para el resultado de las operaciones de sincronización
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncResultDto {
    
    /**
     * ID único del resultado de sincronización
     */
    private UUID syncResultId;
    
    /**
     * Indica si la sincronización fue exitosa
     */
    private boolean success;
    
    /**
     * Mensaje descriptivo del resultado
     */
    private String message;
    
    /**
     * Timestamp del servidor cuando se completó la sincronización
     */
    private LocalDateTime completedAt;
    
    /**
     * Tiempo total de procesamiento en milisegundos
     */
    private long processingTimeMs;
    
    /**
     * Estadísticas de la sincronización
     */
    private SyncStatisticsDto statistics;
    
    /**
     * Lista de errores ocurridos durante la sincronización
     */
    private List<SyncErrorDto> errors;
    
    /**
     * Lista de advertencias durante la sincronización
     */
    private List<SyncWarningDto> warnings;
    
    /**
     * Timestamp recomendado para la próxima sincronización
     */
    private LocalDateTime nextSyncTimestamp;
    
    /**
     * Datos adicionales específicos del resultado
     */
    private Object additionalData;
    
    /**
     * DTO para estadísticas de sincronización
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SyncStatisticsDto {
        private int totalRecordsProcessed;
        private int recordsCreated;
        private int recordsUpdated;
        private int recordsSkipped;
        private int recordsWithErrors;
        
        // Estadísticas por entidad
        private EntityStatisticsDto pacientes;
        private EntityStatisticsDto representantes;
        private EntityStatisticsDto consultas;
        private EntityStatisticsDto actividades;
        private EntityStatisticsDto pacientesRepresentantes;
        private EntityStatisticsDto detallesAntropometricos;
        private EntityStatisticsDto detallesVitales;
        private EntityStatisticsDto detallesMetabolicos;
        private EntityStatisticsDto detallesObstetricia;
        private EntityStatisticsDto detallesPedriatricos;
        private EntityStatisticsDto evaluacionesAntropometricas;
        private EntityStatisticsDto diagnosticos;
    }
    
    /**
     * DTO para estadísticas por entidad
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EntityStatisticsDto {
        private int processed;
        private int created;
        private int updated;
        private int skipped;
        private int errors;
    }
    
    /**
     * DTO para errores de sincronización
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SyncErrorDto {
        private String entityType;
        private UUID entityId;
        private String errorCode;
        private String errorMessage;
        private String fieldName;
        private Object fieldValue;
        private LocalDateTime occurredAt;
    }
    
    /**
     * DTO para advertencias de sincronización
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SyncWarningDto {
        private String entityType;
        private UUID entityId;
        private String warningCode;
        private String warningMessage;
        private String fieldName;
        private Object fieldValue;
        private LocalDateTime occurredAt;
    }
}