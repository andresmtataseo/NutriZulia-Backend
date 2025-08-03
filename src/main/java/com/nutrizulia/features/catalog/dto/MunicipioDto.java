package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name="Municipio",description = "Entidad que representa un municipio dentro de un estado")
public class MunicipioDto {

    @Schema(description = "Identificador único del municipio", example = "326", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador del estado al que pertenece el municipio", example = "23")
    @NotNull(message = "El identificador del estado no puede ser nulo")
    private Integer estado_id;

    @Schema(description = "Nombre del municipio", example = "MARACAIBO")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
