package com.nutrizulia.features.collection.dto;

import com.nutrizulia.common.enums.Estado;
import com.nutrizulia.common.enums.TipoConsulta;
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
@Schema(description = "Entidad que representa una consulta médica o nutricional realizada a un paciente")
public class ConsultaDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador de la relación usuario-institución-rol que realiza la consulta", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;
    
    @Schema(description = "Identificador único del paciente que recibe la consulta", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador del paciente no puede ser nulo")
    private UUID paciente_id;
    
    @Schema(description = "Identificador del tipo de actividad asociada a la consulta", example = "2")
    private Integer tipo_actividad_id;
    
    @Schema(description = "Identificador de la especialidad que remite al paciente", example = "5")
    private Integer especialidad_remitente_id;
    
    @Schema(description = "Tipo de consulta realizada", example = "PRIMERA_CONSULTA")
    @NotNull(message = "El tipo de consulta es obligatorio")
    private TipoConsulta tipo_consulta;
    
    @Schema(description = "Motivo principal por el cual se realiza la consulta", example = "Control nutricional de rutina")
    private String motivo_consulta;
    
    @Schema(description = "Fecha y hora programada para la consulta", example = "2024-05-20T10:30:00")
    private LocalDateTime fecha_hora_programada;
    
    @Schema(description = "Observaciones adicionales sobre la consulta", example = "Paciente presenta buen estado general")
    private String observaciones;
    
    @Schema(description = "Planes de tratamiento o seguimiento establecidos", example = "Dieta hipocalórica, control en 15 días")
    private String planes;
    
    @Schema(description = "Fecha y hora real en que se realizó la consulta", example = "2024-05-20T10:45:00")
    private LocalDateTime fecha_hora_real;
    
    @Schema(description = "Estado actual de la consulta", example = "COMPLETADA")
    @NotNull(message = "El estado de la consulta es obligatorio")
    private Estado estado;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}