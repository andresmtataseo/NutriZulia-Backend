package com.nutrizulia.features.user.dto;

import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.dto.RolDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(name = "Usuario Detalles", description = "Usuario con información completa de instituciones y roles")
public class UsuarioDetallesDto {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Integer id;

    @Schema(description = "Cédula del usuario", example = "V-12345678")
    private String cedula;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez García")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento del usuario")
    private LocalDate fechaNacimiento;

    @Schema(description = "Género del usuario", example = "MASCULINO")
    private String genero;

    @Schema(description = "Teléfono del usuario", example = "0412-1234567")
    private String telefono;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
    private String correo;

    @Schema(description = "Estado del usuario", example = "true")
    private Boolean isEnabled;

    @Schema(description = "Lista de asignaciones de instituciones con entidades completas")
    private List<UsuarioInstitucionDetalleDto> instituciones;

    @Data
    @Schema(name = "Usuario Institución Detalle", description = "Detalle completo de la asignación usuario-institución")
    public static class UsuarioInstitucionDetalleDto {

        @Schema(description = "Identificador único de la asignación", example = "1")
        private Integer id;

        @Schema(description = "Información completa de la institución")
        private InstitucionDto institucion;

        @Schema(description = "Información completa del rol")
        private RolDto rol;

        @Schema(description = "Fecha de inicio en la institución")
        private LocalDate fechaInicio;

        @Schema(description = "Fecha de fin en la institución (null si está activo)")
        private LocalDate fechaFin;

        @Schema(description = "Estado de la asignación", example = "true")
        private Boolean isEnabled;
    }
}