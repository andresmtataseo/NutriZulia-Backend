package com.nutrizulia.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Parametro de Crecimiento Pedriatico por Edad", description = "Entidad que representa los parametros de crecimientos pedriaticos de 0-5 años segun la OMS")
public class ParametroCrecimientoPediatricoEdadDto {

    @Schema(description = "Identificador único del parametro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador foráneo del tipo de indicador", example = "1")
    @NotNull(message = "El identificador foráneo no puede ser nulo")
    private Integer tipo_indicador_id;

    @Schema(description = "Identificador foráneo del grupo etario", example = "1")
    @NotNull(message = "El identificador foráneo no puede ser nulo")
    private Integer grupo_etario_id;

    @Schema(description = "Genero al que aplica el parametro", example = "M")
    @NotBlank(message = "El genero no puede estar en blanco")
    @Size(max = 1, message = "El genero no puede exceder de un caracter")
    private char genero;

    @Schema(description = "Edad en meses a la que aplica el parametro", example = "4")
    @NotNull(message = "La edad en meses no puede ser nula")
    private Integer edad_dia;

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
