package com.nutrizulia.dto.pre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "Rol", description = "Entidad que representa un rol de usuario en el sistema")
public class RolDto {

    @Schema(description = "Identificador único del rol", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Schema(description = "Nombre del rol", example = "ROLE_NUTRICIONISTA_MOVIL")
    private String nombre;
}
