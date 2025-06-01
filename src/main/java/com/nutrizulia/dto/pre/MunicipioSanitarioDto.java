package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name="Municipio Sanitario",description = "Entidad que representa un municipio sanitario dentro de un estado")
public class MunicipioSanitarioDto {
    @Schema(description = "Identificador único del municipio sanitario", example = "14", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador del estado al que pertenece el municipio sanitario", example = "23")
    @NotNull(message = "El identificador del estado no puede ser nulo")
    private Integer estado_id;

    @Schema(description = "Nombre del municipio sanitario", example = "MARACAIBO I")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

}
