package com.nutrizulia.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Entidad que representa los datos obstétricos registrados durante una consulta a pacientes femeninas")
public class DetalleObstetriciaDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenecen estos datos", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    private UUID consulta_id;
    
    @Schema(description = "Indica si la paciente está actualmente embarazada", example = "true")
    private Boolean esta_embarazada;
    
    @Schema(description = "Fecha de la última menstruación", example = "2024-03-15")
    private LocalDate fecha_ultima_menstruacion;
    
    @Schema(description = "Número de semanas de gestación", example = "12")
    private Integer semanas_gestacion;
    
    @Schema(description = "Peso previo al embarazo en kilogramos", example = "60.5")
    private BigDecimal peso_pre_embarazo;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}