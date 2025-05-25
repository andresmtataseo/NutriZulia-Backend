package com.nutrizulia.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String token;
    Integer id;
    String cedula;
    String nombres;
    String apellidos;
    String telefono;
    String correo;
    List<UsuarioInstitucionDto> institucionesRoles;
}
