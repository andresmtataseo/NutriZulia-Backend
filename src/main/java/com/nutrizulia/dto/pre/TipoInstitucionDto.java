package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Tipo de Institucion", description = "Entidad que representa el tipo de institución")
public class TipoInstitucionDto {

    @Schema(description = "Identificador único del tipo de institución", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre del tipo de institución", example = "HOSPITAL TIPO I")
    @NotBlank(message = "El nombre del tipo de institución es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;
}
