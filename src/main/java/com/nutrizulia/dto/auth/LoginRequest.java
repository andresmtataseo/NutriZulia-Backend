package com.nutrizulia.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Login Request", description = "Solicitud de inicio de sesión de usuario")
public class LoginRequest {

    @Schema(description = "Número de cédula del usuario", example = "V-12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La cédula no puede estar vacía")
    @Size(min = 10, max = 10, message = "La cédula debe tener 10 caracteres")
    private String cedula;

    @Schema(description = "Contraseña del usuario", example = "miContraseñaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 4, message = "La contraseña debe tener al menos 6 caracteres")
    private String clave;
}