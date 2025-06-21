package com.nutrizulia.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Especialidad", description = "Entidad que representa una especialidad médica")
public class EspecialidadDto {

    @Schema(description = "Identificador único de la especialidad", example = "54", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre de la especialidad", example = "PEDIATRÍA")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
