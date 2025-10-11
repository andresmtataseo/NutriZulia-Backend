package com.nutrizulia.features.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Serie de datos para gráficos del dashboard")
public record ChartSeriesDto(
        @Schema(description = "Etiqueta de la serie", example = "Consultas") String label,
        @Schema(description = "Arreglo de valores numéricos") double[] data,
        @Schema(description = "Color principal de la serie", example = "#2a9d8f") String color
) {}