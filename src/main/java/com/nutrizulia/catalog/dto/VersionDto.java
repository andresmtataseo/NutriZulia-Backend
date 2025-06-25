package com.nutrizulia.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "Version", description = "Entidad que representa la version de las tablas catalog")
public class VersionDto {

    @Schema(description = "Identificador único de la tabla", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombre_tabla;

    @NotBlank(message = "La version de la tabla es obligatoria")
    @Schema(description = "Version de la tabla", example = "2")
    private Integer version;

}
