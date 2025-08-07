package com.nutrizulia.features.user.controller;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USERS_BASE_URL)
@Tag(
        name = "Gestión de Usuarios",
        description = "Operaciones para crear, editar, desactivar usuarios, asignar instituciones de salud y roles."
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
    @GetMapping(USERS_GET_ALL)
    public ResponseEntity<List<UsuarioDto>> getUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener usuarios con instituciones paginado", 
               description = "Devuelve una lista paginada de usuarios con sus instituciones y roles. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping("/con-instituciones")
    public ResponseEntity<PageResponseDto<UsuarioConInstitucionesDto>> getUsuariosConInstituciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "nombres") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        PageResponseDto<UsuarioConInstitucionesDto> response = usuarioService
                .getUsuariosConInstituciones(page, size, search, sortBy, sortDir);
        
        return ResponseEntity.ok(response);
    }

}
