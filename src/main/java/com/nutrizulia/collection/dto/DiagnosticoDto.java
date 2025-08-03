package com.nutrizulia.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un diagnóstico médico asociado a una consulta")
public class DiagnosticoDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenece este diagnóstico", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    private UUID consulta_id;
    
    @Schema(description = "Identificador del riesgo biológico asociado al diagnóstico", example = "3")
    private Integer riesgo_biologico_id;
    
    @Schema(description = "Identificador de la enfermedad diagnosticada", example = "15")
    private Integer enfermedad_id;
    
    @Schema(description = "Indica si este es el diagnóstico principal de la consulta", example = "true")
    @NotNull(message = "Debe especificarse si es el diagnóstico principal")
    private Boolean is_principal;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}