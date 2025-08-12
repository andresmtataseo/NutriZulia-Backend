package com.nutrizulia.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(name = "Usuario Institucion", description = "Relación entre usuarios, instituciones y sus respectivos roles")
public class UsuarioInstitucionDto {

    @Schema(description = "Identificador único de la relación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador de la institución a la que pertenece el usuario")
    @NotNull(message = "La institución no puede ser nula")
    private Integer institucion_id;

    @Schema(description = "Identificador del usuario asociado a la institución")
    @NotNull(message = "El usuario no puede ser nulo")
    private Integer usuario_id;

    @Schema(description = "Identificador del rol que tiene el usuario dentro de la institución")
    @NotNull(message = "El rol no puede ser nulo")
    private Integer rol_id;

    @Schema(description = "Fecha en la que inicio el usuario con el rol en la institucion")
    @NotNull(message = "La Fecha de inicio no puede ser nula")
    private LocalDate fecha_inicio;

    @Schema(description = "Fecha en la que finalizo el usuario con el rol en la institucion")
    private LocalDate fecha_fin;

    @Schema(description = "Estado del usuario con el rol dentro de la institución")
    @NotNull(message = "El estado del usuario no puede ser nulo")
    private Boolean is_enabled;

}
