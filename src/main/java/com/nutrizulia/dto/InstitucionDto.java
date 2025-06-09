package com.nutrizulia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Entidad que representa una institución de salud")
public class InstitucionDto {

    @Schema(description = "Identificador único de la institución", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador del municipio sanitario al que pertenece la institución", example = "14")
    @NotNull(message = "El identificador del municipio sanitario no puede ser nulo")
    private Integer municipio_sanitario_id;

    @NotNull(message = "Identificador del tipo de institución es obligatorio")
    @Schema(description = "Tipo de institución asociada", example = "2")
    private Integer tipo_institucion_id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre es de máximo 255 caracteres")
    @Schema(description = "Nombre de la institución", example = "HOSPITAL DE ESPECIALIDADES PEDIÁTRICAS")
    private String nombre;
}
