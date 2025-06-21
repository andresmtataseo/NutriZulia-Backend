package com.nutrizulia.institution.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.institution.dto.InstitucionDto;
import com.nutrizulia.institution.service.IInstitucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.CATALOG_API_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.INSTITUTION_API_BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(INSTITUTION_API_BASE_URL)
@Tag(
        name = "Gestión de Instituciones",
        description = "Creación y mantenimiento de instituciones de salud."
)
public class InstitucionController {

    private final IInstitucionService institucionService;

    @Operation(summary = "Obtener lista de instituciones de salud", description = "Devuelve una lista de instituciones de salud. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado - El recurso solicitado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping("v1/instituciones")
    public ResponseEntity<List<InstitucionDto>> getInstituciones() {
        List<InstitucionDto> instituciones = institucionService.getInstituciones();
        return ResponseEntity.ok(instituciones);
    }

}
