package com.nutrizulia.features.reports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InstitucionDataFreshness", description = "Información de frescura de datos por institución incluyendo usuarios activos")
public class InstitucionDataFreshnessDto {

    @Schema(description = "ID de la institución", example = "10")
    private Integer institucionId;

    @Schema(description = "Nombre de la institución", example = "Ambulatorio Urbano IV Las Mercedes")
    private String institucionNombre;

    @Schema(description = "Usuarios con actividad y su última fecha de actualización")
    private List<UsuarioDataFreshnessDto> usuarios;
}