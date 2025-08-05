package com.nutrizulia.features.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la relación entre un paciente y su representante legal")
public class PacienteRepresentanteDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador de la relación usuario-institución-rol que registra esta relación", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;
    
    @Schema(description = "Identificador único del paciente", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador del paciente no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String paciente_id;
    
    @Schema(description = "Identificador único del representante", example = "550e8400-e29b-41d4-a716-446655440002")
    @NotNull(message = "El identificador del representante no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String representante_id;
    
    @Schema(description = "Identificador del tipo de parentesco entre el paciente y el representante", example = "1")
    @NotNull(message = "El parentesco es obligatorio")
    private Integer parentesco_id;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}