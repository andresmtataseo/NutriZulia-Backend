package com.nutrizulia.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Auth Response", description = "Respuesta de autenticación que contiene los datos del usuario y un token de acceso JWT.")
public class AuthResponse {

    @Schema(description = "Token de autenticación JWT para solicitudes seguras.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String token;

    @Schema(description = "Identificador único del usuario.", example = "101")
    private Integer id;

    @Schema(description = "Número de cédula del usuario.", example = "V-12345678")
    private String cedula;

    @Schema(description = "Nombres del usuario.", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario.", example = "Pérez Rodríguez")
    private String apellidos;

    @Schema(description = "Número de teléfono del usuario.", example = "0414-1234567")
    private String telefono;

    @Schema(description = "Dirección de correo electrónico del usuario.", example = "juan.perez@example.com")
    private String correo;

    @Schema(description = "Lista de instituciones a las que pertenece el usuario y sus roles asociados.",
            example = "[{\"id\": 50, \"idUsuario\": 101, \"institucionId\": 1, \"institucionNombre\": \"COORDINACIÓN REGIONAL DE NUTRICIÓN Y DIETÉTICA DEL ESTADO ZULIA\", \"rolId\": 3, \"rolNombre\": \"ROLE_ADMINISTRADOR_WEB\"}, " +
                    "{\"id\": 51, \"idUsuario\": 101, \"institucionId\": 2, \"institucionNombre\": \"HOSPITAL CENTRAL DE MARACAIBO\", \"rolId\": 1, \"rolNombre\": \"ROLE_NUTRICIONISTA_MOVIL\"}]")
    private List<UsuarioInstitucionDto> institucionesRoles;

}