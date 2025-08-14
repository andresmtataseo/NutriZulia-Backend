package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.service.IActividadService;
import com.nutrizulia.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.COLLECTION_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_ACTIVITIES;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class ActividadController {

    private final IActividadService actividadService;

    @Operation(summary = "Sincronizar lista de actividades", description = "Recibe y sincroniza una lista de actividades desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividades sincronizadas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_ACTIVITIES)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncActividades(
            @RequestBody List<ActividadDto> actividades, HttpServletRequest request) {

        try {
            log.info("Iniciando sincronización de {} actividades", actividades.size());
            
            BatchSyncResponseDTO syncResponse = actividadService.sycnActividades(actividades);
            HttpStatus status = determinarEstadoRespuesta(syncResponse);
            String mensaje = construirMensajeRespuesta(syncResponse, "actividades");
            
            log.info("Sincronización completada - Exitosas: {}, Fallidas: {}", 
                    syncResponse.getSuccess().size(), syncResponse.getFailed().size());
            
            return ResponseEntity.status(status).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(status.value())
                            .message(mensaje)
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(syncResponse)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error durante la sincronización de actividades: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error interno durante la sincronización")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }
    
    private HttpStatus determinarEstadoRespuesta(BatchSyncResponseDTO response) {
        if (response.getFailed().isEmpty()) {
            return HttpStatus.OK; // Todos exitosos
        } else if (response.getSuccess().isEmpty()) {
            return HttpStatus.BAD_REQUEST; // Todos fallaron
        } else {
            return HttpStatus.PARTIAL_CONTENT; // Éxito parcial
        }
    }
    
    private String construirMensajeRespuesta(BatchSyncResponseDTO response, String entidad) {
        int exitosos = response.getSuccess().size();
        int fallidos = response.getFailed().size();
        
        if (fallidos == 0) {
            return String.format("Sincronización de %s completada exitosamente (%d procesados)", 
                    entidad, exitosos);
        } else if (exitosos == 0) {
            return String.format("Error en la sincronización de %s (%d fallaron)", 
                    entidad, fallidos);
        } else {
            return String.format("Sincronización de %s completada parcialmente (%d exitosos, %d fallidos)", 
                    entidad, exitosos, fallidos);
        }
    }
}
