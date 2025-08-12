package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.catalog.dto.InstitucionConUsuariosDto;
import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.service.IInstitucionService;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
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
@RequestMapping(CATALOG_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
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
    @GetMapping(CATALOG_INSTITUTIONS)
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

    @Operation(summary = "Obtener instituciones con usuarios paginado",
            description = "Devuelve una lista paginada de instituciones con sus usuarios y roles. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(CATALOG_INSTITUTIONS_GET_ALL_WITH_USERS)
    public ResponseEntity<PageResponseDto<InstitucionConUsuariosDto>> getInstitucionConUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        PageResponseDto<InstitucionConUsuariosDto> response = institucionService.getInstitucionConUsuarios(page, size, search, sortBy, sortDir);

        return ResponseEntity.ok(response);
    }

}
