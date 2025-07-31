package com.nutrizulia.collection.dto;

import com.nutrizulia.common.enums.TipoLactancia;
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
@Schema(description = "Entidad que representa los datos pediátricos específicos registrados durante una consulta a pacientes menores")
public class DetallePedriatricoDto {
    
    @Schema(description = "Identificador único del detalle pediátrico", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenecen estos datos", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    private UUID consulta_id;
    
    @Schema(description = "Indica si el paciente usa biberón para alimentarse", example = "false")
    private Boolean usa_biberon;
    
    @Schema(description = "Tipo de lactancia que recibe el paciente", example = "MATERNA_EXCLUSIVA")
    private TipoLactancia tipo_lactancia;
    
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;
    
    @Schema(description = "Fecha de última actualización del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updated_at;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean is_deleted;
}