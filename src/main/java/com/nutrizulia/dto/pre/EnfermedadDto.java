package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Enfermedad", description = "Entidad que representa una Enfermedad del CIE-10")
public class EnfermedadDto {

    @Schema(description = "Identificador único de la enfermedad", example = "2068", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Codigo internacional del CIE-10", example = "E11")
    @NotBlank(message = "El codigo no puede estar en blanco")
    @Size(max = 4, message = "El codigo internacional no puede exceder los 4 caracteres")
    private String codigo_internacional;

    @Schema(description = "Nombre de la enfermedad", example = "DIABETES MELLITUS TIPO 2")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

    @Schema(description = "Género al que aplica la enfermedad", example = "A")
    @NotBlank(message = "El limite del genero no puede estar en blanco")
    @Size(max = 1, message = "El género no puede exceder de un caracter")
    private char genero;

    @Schema(description = "Edad mínima a la que aplica la enfermedad", example = "028D")
    @NotBlank(message = "La edad mínima no puede estar en blanco")
    @Size(max = 4, message = "La edad mínima no puede exceder los 4 caracteres")
    private String edad_minima;

    @Schema(description = "Edad máxima a la que aplica la enfermedad", example = "120A")
    @NotBlank(message = "La edad máxima no puede estar en blanco")
    @Size(max = 4, message = "La edad máxima no puede exceder los 4 caracteres")
    private String edad_maxima;

}
