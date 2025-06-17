package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
@Schema(name = "Tipo de Indicador", description = "Entidad que representa un tipo de indicador de crecimiento (ej. Peso para la Edad, IMC para la Edad).")
public class TipoIndicadorDto {

    @Schema(description = "Identificador único del tipo de indicador", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre descriptivo del tipo de indicador", example = "PESO/EDAD")
    @NotBlank(message = "El nombre del tipo de indicador no puede estar en blanco")
    @Size(max = 255, message = "El nombre del tipo de indicador no puede exceder de 255 caracteres")
    private String nombre;

}