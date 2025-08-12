package com.nutrizulia.features.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(name = "Institución con Usuarios", description = "Institución con sus usuarios y roles asociados")
public class InstitucionConUsuariosDto {

    @Schema(description = "Identificador único de la institución", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la institución", example = "Hospital Central")
    private String nombre;

    @Schema(description = "Información del municipio sanitario")
    private MunicipioSanitarioDto municipio_sanitario;

    @Schema(description = "Información del tipo de institución")
    private TipoInstitucionDto tipo_institucion;

    @Schema(description = "Lista de usuarios asignados a la institución")
    private List<UsuarioConRolDto> usuarios;

    @Data
    @Schema(name = "Usuario con Rol", description = "Usuario con su rol en la institución")
    public static class UsuarioConRolDto {

        @Schema(description = "Identificador único del usuario", example = "1")
        private Integer id;

        @Schema(description = "Cédula del usuario", example = "V-12345678")
        private String cedula;

        @Schema(description = "Nombres del usuario", example = "Juan Carlos")
        private String nombres;

        @Schema(description = "Apellidos del usuario", example = "Pérez García")
        private String apellidos;

        @Schema(description = "Fecha de nacimiento del usuario")
        private LocalDate fecha_nacimiento;

        @Schema(description = "Género del usuario", example = "MASCULINO")
        private String genero;

        @Schema(description = "Teléfono del usuario", example = "0412-1234567")
        private String telefono;

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
        private String correo;

        @Schema(description = "Estado del usuario", example = "true")
        private Boolean is_enabled;

        @Schema(description = "Información de la asignación del usuario a la institución")
        private UsuarioInstitucionDto usuario_institucion;

        @Data
        @Schema(name = "Usuario Institución", description = "Información de la asignación del usuario a la institución")
        public static class UsuarioInstitucionDto {

            @Schema(description = "Identificador único de la asignación", example = "1")
            private Integer id;

            @Schema(description = "Identificador de la institución", example = "1")
            private Integer institucion_id;

            @Schema(description = "Información del rol asignado")
            private RolDto rol;

            @Schema(description = "Fecha de inicio de la asignación")
            private LocalDate fecha_inicio;

            @Schema(description = "Fecha de fin de la asignación")
            private LocalDate fecha_fin;

            @Schema(description = "Estado de la asignación", example = "true")
            private Boolean is_enabled;
        }
    }

    @Data
    @Schema(name = "Municipio Sanitario", description = "Información del municipio sanitario")
    public static class MunicipioSanitarioDto {

        @Schema(description = "Identificador único del municipio sanitario", example = "1")
        private Integer id;

        @Schema(description = "Nombre del municipio sanitario", example = "Maracaibo")
        private String nombre;
    }

    @Data
    @Schema(name = "Tipo Institución", description = "Información del tipo de institución")
    public static class TipoInstitucionDto {

        @Schema(description = "Identificador único del tipo de institución", example = "1")
        private Integer id;

        @Schema(description = "Nombre del tipo de institución", example = "Hospital")
        private String nombre;
    }
}