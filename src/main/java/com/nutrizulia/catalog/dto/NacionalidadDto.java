package com.nutrizulia.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Nacionalidad", description = "Entidad que representa una nacionalidad")
public class NacionalidadDto {

    @Schema(description = "Identificador único de la nacionalidad", example = "239", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre de la nacionalidad", example = "VENEZUELA")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
