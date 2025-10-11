package com.nutrizulia.features.dashboard.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta estándar para gráficos del dashboard compatible con Chart.js")
public record ChartResponseDto(
        @Schema(description = "Título dinámico del gráfico", example = "Consultas por mes: Ene 2024 - Jun 2024") String title,
        @ArraySchema(arraySchema = @Schema(description = "Etiquetas para el eje X"), schema = @Schema(example = "Ene")) List<String> labels,
        @ArraySchema(arraySchema = @Schema(description = "Series de datos")) List<ChartSeriesDto> series
) {}