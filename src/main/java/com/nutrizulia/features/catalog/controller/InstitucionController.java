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
import jakarta.validation.Valid;
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

    @Operation(summary = "Crear nueva institución", description = "Crea una nueva institución de salud. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institución creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltan parámetros requeridos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe una institución con el mismo nombre", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(CATALOG_INSTITUTIONS)
    public ResponseEntity<ApiResponseDto<InstitucionDto>> createInstitucion(
            @RequestBody @Valid InstitucionDto institucionDto,
            HttpServletRequest request
    ) {
        InstitucionDto nuevaInstitucion = institucionService.createInstitucion(institucionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.<InstitucionDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Institución creada exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(nuevaInstitucion)
                        .build()
        );
    }

    @Operation(summary = "Actualizar institución", description = "Actualiza una institución existente. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltan parámetros requeridos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe una institución con el mismo nombre", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PutMapping(CATALOG_INSTITUTIONS + "/{id}")
    public ResponseEntity<ApiResponseDto<InstitucionDto>> updateInstitucion(
            @PathVariable Integer id,
            @RequestBody @Valid InstitucionDto institucionDto,
            HttpServletRequest request
    ) {
        InstitucionDto institucionActualizada = institucionService.updateInstitucion(id, institucionDto);
        return ResponseEntity.ok(
                ApiResponseDto.<InstitucionDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Institución actualizada exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(institucionActualizada)
                        .build()
        );
    }

    @Operation(summary = "Eliminar institución", description = "Elimina una institución existente. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - La institución tiene usuarios asignados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @DeleteMapping(CATALOG_INSTITUTIONS + "/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteInstitucion(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        institucionService.deleteInstitucion(id);
        return ResponseEntity.ok(
                ApiResponseDto.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Institución eliminada exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
        );
    }

}
