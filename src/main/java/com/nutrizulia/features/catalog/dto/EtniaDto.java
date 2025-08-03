package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Etnia", description = "Entidad que representa una etnia")
public class EtniaDto {

    @Schema(description = "Identificador único de la etnia", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre de la etnia", example = "WAYUU")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;
}
