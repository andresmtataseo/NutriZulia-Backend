package com.nutrizulia.features.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavePhoneDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^(0414|0424|0412|0416|0426)-[0-9]{7}$", 
             message = "El teléfono debe tener el formato válido venezolano (0414, 0424, 0412, 0416, 0426 seguido de guión y 7 dígitos)")
    private String telefono;
}