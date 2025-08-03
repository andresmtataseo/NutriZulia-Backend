package com.nutrizulia.features.auth.dto;

import com.nutrizulia.features.auth.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
@Schema(description = "DTO para el cambio de contraseña del usuario")
public class ChangePasswordRequestDto {

    @Schema(
            description = "Contraseña actual del usuario",
            example = "MiClaveActual123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String claveActual;

    @Schema(
            description = "Nueva contraseña del usuario. Debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una minúscula, un número y un carácter especial",
            example = "MiNuevaClave456!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 50, message = "La nueva contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La nueva contraseña debe contener al menos: una letra minúscula, una mayúscula, un número y un carácter especial (@$!%*?&)"
    )
    private String claveNueva;

    @Schema(
            description = "Confirmación de la nueva contraseña. Debe coincidir exactamente con la nueva contraseña",
            example = "MiNuevaClave456!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
    private String claveNuevaConfirmacion;
}