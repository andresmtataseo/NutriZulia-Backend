package com.nutrizulia.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(name = "Usuario con Instituciones", description = "Usuario con sus instituciones y roles asociados")
public class UsuarioConInstitucionesDto {

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

    @Schema(description = "Lista de instituciones y roles del usuario")
    private List<InstitucionRolDto> instituciones;
}