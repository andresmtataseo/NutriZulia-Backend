package com.nutrizulia.collection.dto;

import com.nutrizulia.common.enums.TipoValorCalculado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la evaluación antropométrica calculada a partir de las medidas registradas")
public class EvaluacionAntropometricaDto {
    
    @Schema(description = "Identificador único de la evaluación antropométrica", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    
    @Schema(description = "Identificador único de la consulta asociada", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    private UUID consulta_id;
    
    @Schema(description = "Identificador único del detalle antropométrico base para esta evaluación", example = "550e8400-e29b-41d4-a716-446655440002")
    @NotNull(message = "El identificador del detalle antropométrico no puede ser nulo")
    private UUID detalle_antropometrico_id;
    
    @Schema(description = "Identificador del tipo de indicador antropométrico evaluado", example = "1")
    @NotNull(message = "El tipo de indicador es obligatorio")
    private Integer tipo_indicador_id;
    
    @Schema(description = "Valor calculado del indicador antropométrico", example = "22.5")
    private BigDecimal valor_calculado;
    
    @Schema(description = "Tipo de valor calculado (percentil, z-score, etc.)", example = "PERCENTIL")
    private TipoValorCalculado tipo_valor_calculado;
    
    @Schema(description = "Diagnóstico antropométrico basado en la evaluación", example = "Peso normal para la edad")
    private String diagnostico_antropometrico;
    
    @Schema(description = "Fecha en que se realizó la evaluación", example = "2024-05-20")
    @NotNull(message = "La fecha de evaluación es obligatoria")
    private LocalDate fecha_evaluacion;
    
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;
    
    @Schema(description = "Fecha de última actualización del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updated_at;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean is_deleted;
}