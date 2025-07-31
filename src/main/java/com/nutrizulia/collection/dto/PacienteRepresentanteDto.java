package com.nutrizulia.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la relación entre un paciente y su representante legal")
public class PacienteRepresentanteDto {
    
    @Schema(description = "Identificador único de la relación paciente-representante", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    
    @Schema(description = "Identificador de la relación usuario-institución-rol que registra esta relación", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;
    
    @Schema(description = "Identificador único del paciente", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador del paciente no puede ser nulo")
    private UUID paciente_id;
    
    @Schema(description = "Identificador único del representante", example = "550e8400-e29b-41d4-a716-446655440002")
    @NotNull(message = "El identificador del representante no puede ser nulo")
    private UUID representante_id;
    
    @Schema(description = "Identificador del tipo de parentesco entre el paciente y el representante", example = "1")
    @NotNull(message = "El parentesco es obligatorio")
    private Integer parentesco_id;
    
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;
    
    @Schema(description = "Fecha de última actualización del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updated_at;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean is_deleted;
}