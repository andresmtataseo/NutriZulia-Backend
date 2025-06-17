package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "GrupoEtario", description = "Entidad que representa un Grupo Etario para la Evaluacion Antropometrica")
public class GrupoEtarioDto {

    @Schema(description = "Identificador único del grupo etario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre del grupo etario", example = "0 a 5 AÑOS")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

    @Schema(description = "Edad mínima del grupo etario en mes", example = "0")
    @NotNull(message = "La edad mínima no puede ser nula")
    private Integer edad_mes_minima;

    @Schema(description = "Edad máxima del grupo etario en mes", example = "28")
    @NotNull(message = "La edad máxima no puede ser nula")
    private Integer edad_mes_maxima;
}