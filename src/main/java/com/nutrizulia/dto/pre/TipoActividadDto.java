package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Tipo de Actividad", description = "Entidad que representa un tipo de actividad médica")
public class TipoActividadDto {

    @Schema(description = "Identificador único del tipo de actividad", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre del tipo de actividad", example = "JORNADA")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
