package com.nutrizulia.userinstitution.controller;

import com.nutrizulia.catalog.dto.EnfermedadDto;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.userinstitution.dto.UsuarioInstitucionDto;
import com.nutrizulia.userinstitution.service.UsuarioInstitucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(USER_INSTITUTION_API_BASE_URL)
@Tag(
        name = "Gestión Usuario-Institución",
        description = "Gestión de asignaciones entre usuarios del sistema e instituciones registradas."
)
public class UsuarioInstitucionController {

    private final UsuarioInstitucionService usuarioInstitucionService;

    @Operation(
            summary = "Obtener todas las asignaciones a instituciones",
            description = "Permite recuperar una lista de todas las instituciones afiliadas al usuario. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioInstitucionDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USER_INSTITUTION_ALL_BY_ID_USER)
    public ResponseEntity<List<UsuarioInstitucionDto>> getInstitucionesByUsuarioId(
            @Valid
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario) {
        return ResponseEntity.ok(usuarioInstitucionService.getInstitucionesByUsuarioId(idUsuario));
    }

}
