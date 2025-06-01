package com.nutrizulia.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Institucion y Rol del Usuario", description = "Representa la asociación de un usuario con una institución específica y su rol dentro de ella.")
public class UsuarioInstitucionDto {

    @Schema(description = "Identificador único de la relación usuario-institución.", example = "50")
    private Integer id;

    @Schema(description = "Identificador único del usuario asociado.", example = "101")
    private Integer idUsuario;

    @Schema(description = "Identificador único de la institución.", example = "1")
    private Integer institucionId;

    @Schema(description = "Nombre de la institución a la que el usuario está asociado.", example = "Hospital Central de Maracaibo")
    private String institucionNombre;

    @Schema(description = "Identificador único del rol que el usuario tiene dentro de esta institución.", example = "3")
    private Integer rolId;

    @Schema(description = "Nombre descriptivo del rol del usuario en la institución.", example = "ROLE_NUTRICIONISTA_MOVIL")
    private String rolNombre;

}