package com.nutrizulia.features.auth.dto;

import com.nutrizulia.features.catalog.model.Rol;
import com.nutrizulia.features.user.dto.UsuarioDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para la autenticación, incluyendo el token JWT, los datos del usuario y rol")
public class AuthAdminResponseDto {

    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwiaWF0IjoxNjM1NjgyNjQ3LCJleHAiOjE2MzU2ODYyNDd9.somehashstring")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String type;

    @Schema(description = "Datos del usuario autenticado")
    private UsuarioDto user;

    @Schema(description = "Rol del usuario autenticado")
    private Rol rol;
}
