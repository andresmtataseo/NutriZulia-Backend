package com.nutrizulia.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Update Usuario Institucion", description = "DTO para actualizar asignación de usuario a institución")
public class UpdateUsuarioInstitucionDto {

    @Schema(description = "Identificador del rol que tiene el usuario dentro de la institución")
    @NotNull(message = "El rol no puede ser nulo")
    private Integer rol_id;

    @Schema(description = "Estado del usuario con el rol dentro de la institución")
    @NotNull(message = "El estado no puede ser nulo")
    private Boolean is_enabled;

}