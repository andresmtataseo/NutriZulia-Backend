package com.nutrizulia.features.institution.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.EnfermedadDto;
import com.nutrizulia.features.institution.dto.InstitucionDto;
import com.nutrizulia.features.institution.service.IInstitucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(INSTITUTIONS_BASE_URL)
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
    @GetMapping(INSTITUTIONS_GET_ALL)
    public ResponseEntity<ApiResponseDto<List<InstitucionDto>>> getInstituciones(
            HttpServletRequest request
    ) {
        List<InstitucionDto> instituciones = institucionService.getInstituciones();
        return ResponseEntity.ok(
                ApiResponseDto.<List<InstitucionDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lista de instituciones recuperada exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(instituciones)
                        .build()
        );
    }

}
