package com.nutrizulia.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UpdateUsuario", description = "DTO para actualizar datos de un usuario")
public class UpdateUsuarioDto {

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

    @Schema(description = "Fecha de nacimiento del usuario")
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;

    @Schema(description = "Género del usuario")
    @NotBlank(message = "El género no puede estar en blanco")
    private String genero;

    @Schema(description = "Teléfono del usuario", example = "0412-1234567")
    @Size(min = 12, max = 12, message = "El teléfono debe tener exactamente 12 caracteres")
    private String telefono;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@dominio.com")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 255, message = "El email no puede exceder los 255 caracteres")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @Schema(description = "Estado del usuario", example = "true")
    @NotNull(message = "El estado del usuario es obligatorio")
    private Boolean is_enabled;
}