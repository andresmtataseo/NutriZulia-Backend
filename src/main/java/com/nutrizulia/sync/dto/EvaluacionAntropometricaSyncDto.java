package com.nutrizulia.sync.dto;

import com.nutrizulia.common.enums.TipoValorCalculado;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad EvaluacionAntropometrica
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionAntropometricaSyncDto {
    
    private UUID id;
    private UUID consultaId;
    private UUID detalleAntropometricoId;
    private Integer tipoIndicadorId;
    private BigDecimal valorCalculado;
    private TipoValorCalculado tipoValorCalculado;
    private String diagnosticoAntropometrico;
    private LocalDate fechaEvaluacion;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}