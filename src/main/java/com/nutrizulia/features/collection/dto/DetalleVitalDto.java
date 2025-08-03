package com.nutrizulia.features.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa los signos vitales registrados durante una consulta médica")
public class DetalleVitalDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenecen estos signos vitales", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    private UUID consulta_id;
    
    @Schema(description = "Tensión arterial sistólica en mmHg", example = "120")
    private Integer tension_arterial_sistolica;
    
    @Schema(description = "Tensión arterial diastólica en mmHg", example = "80")
    private Integer tension_arterial_diastolica;
    
    @Schema(description = "Frecuencia cardíaca en latidos por minuto", example = "72")
    private Integer frecuencia_cardiaca;
    
    @Schema(description = "Frecuencia respiratoria en respiraciones por minuto", example = "16")
    private Integer frecuencia_respiratoria;
    
    @Schema(description = "Temperatura corporal en grados Celsius", example = "36.5")
    private BigDecimal temperatura;
    
    @Schema(description = "Saturación de oxígeno en porcentaje", example = "98")
    private Integer saturacion_oxigeno;
    
    @Schema(description = "Pulso en latidos por minuto", example = "72")
    private Integer pulso;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}