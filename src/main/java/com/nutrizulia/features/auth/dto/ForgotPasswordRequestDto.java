package com.nutrizulia.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para solicitud de contraseña")
public class ForgotPasswordRequestDto {

    @NotBlank(message = "La cedula es obligatoria")
    @Size(max = 10, message = "La cedula no puede exceder los 10 caracteres")
    @Schema(
            description = "Cedula del usuario que solicita la contraseña",
            example = "V-12345678",
            maxLength = 10,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String cedula;
}