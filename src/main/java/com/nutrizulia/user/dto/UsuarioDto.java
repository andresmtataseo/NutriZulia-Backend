package com.nutrizulia.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;


@Data
@Schema(name = "Usuario", description = "Entidad que representa a un usuario del sistema")
public class UsuarioDto {

    @Schema(description = "Identificador único del usuario", example = "4", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Cédula del usuario", example = "V-12345678")
    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 10, message = "La cédula no debe exceder los 10 caracteres")
    private String cedula;

    @Schema(description = "Nombres del usuario", example = "JOSE RAMON")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "PEREZ TORRES")
    @NotBlank(message = "El apellido no puede estar en blanco")
    @Size(max = 255, message = "El apellido no puede exceder los 255 caracteres")
    private String apellidos;

    private LocalDate fechaNacimiento;

    private String genero;

    @Schema(description = "Teléfono del usuario", example = "0412-1234567")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 12, max = 12, message = "El teléfono no debe exceder los 12 caracteres")
    private String telefono;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@dominio.com")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 255, message = "El email no puede exceder los 255 caracteres")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @Schema(description = "Contraseña del usuario", example = "12345678")
    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 8, message = "La clave debe tener al menos 8 caracteres")
    private String clave;

    @Schema(description = "Estado del usuario", example = "true")
    @NotNull(message = "El estado del usuario es obligatorio")
    private Boolean is_enabled;

}
