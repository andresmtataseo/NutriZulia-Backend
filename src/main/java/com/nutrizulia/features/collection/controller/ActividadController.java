package com.nutrizulia.features.collection.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.service.IActividadService;
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

import static com.nutrizulia.common.util.ApiConstants.COLLECTION_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_ACTIVITIES;

@RestController
@RequiredArgsConstructor
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class ActividadController {

    private final IActividadService actividadService;

    @Operation(summary = "Obtener lista de actividades", description = "Obtiene todas las actividades disponibles. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de actividades obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping("/actividades")
    public ResponseEntity<ApiResponseDto<List<ActividadDto>>> getActividades(HttpServletRequest request) {
        List<ActividadDto> actividades = actividadService.getActividades();
        
        return ResponseEntity.ok(
                ApiResponseDto.<List<ActividadDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lista de actividades obtenida exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(actividades)
                        .build()
        );
    }

    @Operation(summary = "Sincronizar lista de actividades", description = "Recibe y sincroniza una lista de actividades desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividades sincronizadas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_ACTIVITIES)
    public ResponseEntity<ApiResponseDto<List<ActividadDto>>> syncActividades(
            @RequestBody List<ActividadDto> actividades, HttpServletRequest request) {

        List<ActividadDto> actividadesDesdeServidor = actividadService.sycnActividades(actividades);

        return ResponseEntity.ok(
                ApiResponseDto.<List<ActividadDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Sincronización de actividades completada")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(actividadesDesdeServidor)
                        .build()
        );
    }
}
