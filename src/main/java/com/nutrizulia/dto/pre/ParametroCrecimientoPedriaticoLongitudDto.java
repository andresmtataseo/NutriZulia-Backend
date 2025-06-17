package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Parámetro de Crecimiento Pediátrico por Longitud", description = "Entidad que representa los parámetros de crecimiento pediátrico basados en la longitud, según la OMS, para niños de 0 a 5 años.")
public class ParametroCrecimientoPedriaticoLongitudDto {

    @Schema(description = "Identificador único del parámetro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador foráneo del tipo de indicador (ej. Peso para la Longitud, Talla para la Edad)", example = "1")
    @NotNull(message = "El identificador foráneo no puede ser nulo")
    private Integer tipo_indicador_id;

    @Schema(description = "Identificador foráneo del grupo etario al que aplica el parámetro", example = "1")
    @NotNull(message = "El identificador foráneo no puede ser nulo")
    private Integer grupo_etario_id;

    @Schema(description = "Género al que aplica el parámetro (M para Masculino, F para Femenino)", example = "M", allowableValues = {"M", "F"})
    @NotBlank(message = "El género no puede estar en blanco")
    @Size(max = 1, message = "El género no puede exceder de un carácter")
    private char genero;

    @Schema(description = "Longitud en centímetros a la que aplica el parámetro", example = "50")
    @NotNull(message = "La longitud en centímetros no puede ser nula")
    private Integer longitud_cm;

    @Schema(description = "Tipo de medición de la longitud (T para Talla (acostado), A para Altura (de pie))", example = "A", allowableValues = {"A", "T"})
    @NotBlank(message = "El tipo de medición no puede estar en blanco")
    @Size(max = 1, message = "El tipo de medición no puede exceder de un carácter")
    private char tipo_medicion;

    @Schema(description = "Valor de la media (L) en el método LMS, que representa la mediana o el valor central de la distribución", example = "0.0")
    @NotNull(message = "Lambda no puede ser nulo")
    private BigDecimal lambda;

    @Schema(description = "Valor del coeficiente de variación (M) en el método LMS, que indica la asimetría de la distribución", example = "0.0")
    @NotNull(message = "Mu no puede ser nulo")
    private BigDecimal mu;

    @Schema(description = "Valor de la desviación estándar (S) en el método LMS, que describe la dispersión de los datos alrededor de la media", example = "1.0")
    @NotNull(message = "Sigma no puede ser nulo")
    private BigDecimal sigma;
}