package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Estado", description = "Entidad que representa un estado de Venezuela")
public class EstadoDto {

    @Schema(description = "Identificador único del estado", example = "23", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre del estado", example = "ZULIA")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
