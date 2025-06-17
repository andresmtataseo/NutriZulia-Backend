package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Regla de Interpretación del IMC", description = "Entidad que representa las reglas para la interpretación del resultado del Índice de Masa Corporal (IMC).")
public class ReglaInterpretacionImcDto {

    @Schema(description = "Identificador único de la regla de interpretación del IMC", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Tipo de indicador asociado a esta regla de IMC (por ejemplo, IMC para la edad, IMC para la longitud)", example = "{\"id\": 1, \"nombre\": \"IMC para la edad\"}")
    @NotNull(message = "El identificador foráneo del tipo de indicador no puede ser nulo")
    private Integer tipo_indicador_id;

    @Schema(description = "Valor mínimo del IMC para esta regla de interpretación", example = "18.5")
    @NotNull(message = "El valor mínimo del IMC no puede ser nulo")
    private BigDecimal imc_minimo;

    @Schema(description = "Valor máximo del IMC para esta regla de interpretación", example = "24.9")
    @NotNull(message = "El valor máximo del IMC no puede ser nulo")
    private BigDecimal imc_maximo;

    @Schema(description = "Descripción de la interpretación del rango de IMC (ej. 'Peso Normal', 'Sobrepeso')", example = "Peso Normal")
    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

}