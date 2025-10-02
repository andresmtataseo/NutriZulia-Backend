package com.nutrizulia.features.reports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar el resumen de consultas por rango de edad y prenatal,
 * separando el total de primeras consultas y consultas sucesivas.
 * Coincide con la salida de la consulta nativa en ReportsQueryRepositoryImpl.obtenerResumenConsultasPorEdad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resumen de consultas por rango de edad o prenatal, desglosado por tipo de consulta")
public class ResumenConsultasEdadDto {

    @Schema(description = "Rango de edad o categoría (LT2, A2_6, A7_12, A12_19, A19_60, GTE60, PRENATAL)", example = "A2_6")
    @NotBlank(message = "El rango no puede estar vacío")
    private String rango;

    @Schema(description = "Total de consultas de primera vez en el rango", example = "12")
    @NotNull(message = "El total de primeras no puede ser nulo")
    @Min(value = 0, message = "El total de primeras debe ser mayor o igual a 0")
    private Integer totalPrimeras;

    @Schema(description = "Total de consultas sucesivas en el rango", example = "8")
    @NotNull(message = "El total de sucesivas no puede ser nulo")
    @Min(value = 0, message = "El total de sucesivas debe ser mayor o igual a 0")
    private Integer totalSucesivas;
}