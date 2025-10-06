package com.nutrizulia.features.reports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UsuarioDataFreshness", description = "Información de frescura de datos por usuario e institución")
public class UsuarioDataFreshnessDto {

    @Schema(description = "ID del registro usuario-institución", example = "123")
    private Integer usuarioInstitucionId;

    @Schema(description = "ID del usuario", example = "45")
    private Integer usuarioId;

    @Schema(description = "Cédula del usuario", example = "V-12345678")
    private String cedula;

    @Schema(description = "Nombres del usuario", example = "María José")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez Gómez")
    private String apellidos;

    @Schema(description = "Nombre del rol asignado", example = "Nutricionista")
    private String rolNombre;

    @Schema(description = "Fecha y hora de la última actualización registrada", example = "2025-10-06T14:35:00")
    private LocalDateTime ultimaActualizacion;
}