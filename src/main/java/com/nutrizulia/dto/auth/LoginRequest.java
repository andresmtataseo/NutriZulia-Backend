package com.nutrizulia.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "La cedula es obligatoria")
    String cedula;
    @NotBlank(message = "La clave es obligatoria")
    String clave;
}
