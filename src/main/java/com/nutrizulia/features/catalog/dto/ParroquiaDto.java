package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Parroquia", description = "Entidad que representa una parroquia dentro de un municipio")
public class ParroquiaDto {

    @Schema(description = "Identificador único de la parroquia", example = "1076", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador del municipio al que pertenece la parroquia", example = "326")
    @NotNull(message = "El identificador del municipio no puede ser nulo")
    private Integer municipio_id;

    @Schema(description = "Nombre de la parroquia", example = "SANTA LUCIA")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;
}
