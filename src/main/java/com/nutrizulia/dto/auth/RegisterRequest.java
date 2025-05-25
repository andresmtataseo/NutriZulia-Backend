package com.nutrizulia.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String cedula;
    String nombres;
    String apellidos;
    String telefono;
    String correo;
    String clave;
}