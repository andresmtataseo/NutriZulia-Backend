package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Riesgo Biológico", description = "Entidad que representa un factor de riesgo biológico.")
public class RiesgoBiologicoDto {

    @Schema(description = "Identificador único del riesgo biológico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre o descripción del riesgo biológico", example = "Bajo Peso al Nacer")
    @NotBlank(message = "El nombre del riesgo biológico no puede estar en blanco")
    @Size(max = 255, message = "El nombre del riesgo biológico no puede exceder de 255 caracteres")
    private String nombre;

    @Schema(description = "Género al que aplica el riesgo biológico (M para Masculino, F para Femenino, A para Ambos)", example = "F", allowableValues = {"M", "F", "A"})
    @NotBlank(message = "El género no puede estar en blanco")
    @Size(max = 1, message = "El género no puede exceder de un carácter")
    private char genero;

    @Schema(description = "Edad mínima en meses para la aplicación de este riesgo biológico", example = "0")
    @Min(value = 0, message = "La edad mínima en meses debe ser mayor o igual a 0")
    private Integer edad_mes_minima;

    @Schema(description = "Edad máxima en meses para la aplicación de este riesgo biológico", example = "60")
    @Min(value = 0, message = "La edad máxima en meses debe ser mayor o igual a 0")
    private Integer edad_mes_maxima;
}