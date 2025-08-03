package com.nutrizulia.features.user.controller;

import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.user.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.USER_ALL_URL;
import static com.nutrizulia.common.util.ApiConstants.USER_API_BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER_API_BASE_URL)
@Tag(
        name = "Gestión de Usuarios",
        description = "Operaciones para crear, editar, desactivar usuarios y asignar roles o permisos."
)
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @Operation(summary = "Obtener lista de usuarios de salud", description = "Devuelve una lista de usuarios de salud. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado - El recurso solicitado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USER_ALL_URL)
    public ResponseEntity<List<UsuarioDto>> getUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

}
