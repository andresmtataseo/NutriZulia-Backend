package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Regla de Interpretación por Percentil", description = "Entidad que representa las reglas para la interpretación de resultados basados en percentiles, según los estándares de crecimiento.")
public class ReglaInterpretacionPercentilDto {

    @Schema(description = "Identificador único de la regla de interpretación por percentil", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Tipo de indicador asociado a esta regla de percentil (por ejemplo, Peso para la Edad, Talla para la Edad)", example = "{\"id\": 1, \"nombre\": \"Peso para la Edad\"}")
    @NotNull(message = "El tipo de indicador no puede ser nulo")
    private Integer tipo_indicador_id;

    @Schema(description = "Valor mínimo del percentil para esta regla de interpretación", example = "3.0")
    @NotNull(message = "El percentil mínimo no puede ser nulo")
    private BigDecimal percentil_minimo;

    @Schema(description = "Valor máximo del percentil para esta regla de interpretación", example = "85.0")
    @NotNull(message = "El percentil máximo no puede ser nulo")
    private BigDecimal percentil_maximo;

    @Schema(description = "Descripción de la interpretación del rango de percentil (ej. 'Bajo peso', 'Normal', 'Sobrepeso')", example = "Normal")
    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

}