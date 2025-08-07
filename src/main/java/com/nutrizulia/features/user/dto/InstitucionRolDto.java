package com.nutrizulia.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(name = "Institución y Rol", description = "Información de la institución y rol asociado al usuario")
public class InstitucionRolDto {

    @Schema(description = "Identificador de la institución", example = "1")
    private Integer institucionId;

    @Schema(description = "Nombre de la institución", example = "Hospital Central")
    private String institucionNombre;

    @Schema(description = "Identificador del rol", example = "2")
    private Integer rolId;

    @Schema(description = "Nombre del rol", example = "NUTRICIONISTA")
    private String rolNombre;

    @Schema(description = "Fecha de inicio en la institución")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin en la institución (null si está activo)")
    private LocalDate fechaFin;

    @Schema(description = "Estado de la relación usuario-institución", example = "true")
    private Boolean isEnabled;
}