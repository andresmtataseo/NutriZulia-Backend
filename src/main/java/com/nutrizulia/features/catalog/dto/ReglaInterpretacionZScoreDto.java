package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Regla de Interpretación Z-Score", description = "Entidad que representa las reglas para la interpretación de resultados basados en Z-Scores, según los estándares de crecimiento de la OMS.")
public class ReglaInterpretacionZScoreDto {

    @Schema(description = "Identificador único de la regla de interpretación por Z-Score", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador foráneo del tipo de indicador (por ejemplo, Peso para la Edad, Talla para la Edad, IMC para la Edad)", example = "1")
    @NotNull(message = "El identificador del tipo de indicador no puede ser nulo")
    private Integer tipo_indicador_id;

    @Schema(description = "Valor mínimo del Z-Score para esta regla de interpretación", example = "-2.0")
    @NotNull(message = "El Z-Score mínimo no puede ser nulo")
    private BigDecimal z_score_minimo;

    @Schema(description = "Valor máximo del Z-Score para esta regla de interpretación", example = "2.0")
    @NotNull(message = "El Z-Score máximo no puede ser nulo")
    private BigDecimal z_score_maximo;

    @Schema(description = "Descripción de la interpretación del rango de Z-Score (ej. 'Normal', 'Bajo peso severo', 'Sobrepeso')", example = "Normal")
    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

}