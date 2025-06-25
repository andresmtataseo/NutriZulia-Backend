package com.nutrizulia.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "Regex", description = "Entidad que representa la expresión regular")
public class RegexDto {

    @Schema(description = "Identificador único de la expresión regular", example = "CEDULA", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombre;

    @NotBlank(message = "La expresión regular es obligatoria")
    @Schema(description = "Expresión regular", example = "^(?i)[VE]-\\d{1,8}(-\\d{2})?$")
    private String expression;

}
