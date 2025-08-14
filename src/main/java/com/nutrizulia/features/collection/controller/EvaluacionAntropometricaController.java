package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;
import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.service.IEvaluacionAntropometricaService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class EvaluacionAntropometricaController {

    private final IEvaluacionAntropometricaService evaluacionAntropometricaService;

    @Operation(summary = "Sincronizar lista de evaluaciones antropométricas", description = "Recibe y sincroniza una lista de evaluaciones antropométricas desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluaciones antropométricas sincronizadas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_ANTHROPOMETRIC_EVALUATIONS)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncEvaluacionesAntropometricas(
            @RequestBody List<EvaluacionAntropometricaDto> evaluacionesAntropometricas, HttpServletRequest request) {

        try {
            log.info("Iniciando sincronización de {} evaluaciones antropométricas", evaluacionesAntropometricas.size());

            // Validar entrada vacía
            if (evaluacionesAntropometricas.isEmpty()) {
                log.warn("Lista de evaluaciones antropométricas vacía recibida");
                return ResponseEntity.badRequest().body(
                        ApiResponseDto.<BatchSyncResponseDTO>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("La lista de evaluaciones antropométricas no puede estar vacía")
                                .timestamp(LocalDateTime.now())
                                .path(request.getRequestURI())
                                .build()
                );
            }

            BatchSyncResponseDTO resultado = evaluacionAntropometricaService.syncEvaluacionesAntropometrica(evaluacionesAntropometricas);
            HttpStatus status = determinarEstadoRespuesta(resultado);
            String mensaje = construirMensajeRespuesta(resultado);

            log.info("Sincronización completada - Éxitos: {}, Fallos: {}", 
                    resultado.getSuccess().size(), resultado.getFailed().size());

            return ResponseEntity.status(status).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(status.value())
                            .message(mensaje)
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(resultado)
                            .build()
            );

        } catch (Exception e) {
            log.error("Error inesperado durante la sincronización de evaluaciones antropométricas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error interno del servidor durante la sincronización")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }

    private HttpStatus determinarEstadoRespuesta(BatchSyncResponseDTO resultado) {
        if (resultado.getFailed().isEmpty()) {
            return HttpStatus.OK; // 200 - Todos exitosos
        } else if (!resultado.getSuccess().isEmpty()) {
            return HttpStatus.PARTIAL_CONTENT; // 206 - Algunos exitosos, algunos fallidos
        } else {
            return HttpStatus.BAD_REQUEST; // 400 - Todos fallidos
        }
    }

    private String construirMensajeRespuesta(BatchSyncResponseDTO resultado) {
        int exitosos = resultado.getSuccess().size();
        int fallidos = resultado.getFailed().size();
        int total = exitosos + fallidos;

        if (fallidos == 0) {
            return String.format("Sincronización completada exitosamente: %d/%d evaluaciones antropométricas procesadas", exitosos, total);
        } else if (exitosos == 0) {
            return String.format("Sincronización fallida: %d/%d evaluaciones antropométricas no pudieron ser procesadas", fallidos, total);
        } else {
            return String.format("Sincronización parcial: %d/%d exitosos, %d/%d fallidos", exitosos, total, fallidos, total);
        }
    }

    @Operation(summary = "Obtener todas las evaluaciones antropométricas activas", description = "Obtiene todas las evaluaciones antropométricas activas para sincronización completa. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluaciones antropométricas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(COLLECTION_SYNC_ANTHROPOMETRIC_EVALUATIONS_FULL)
    public ResponseEntity<ApiResponseDto<FullSyncResponseDTO<EvaluacionAntropometricaDto>>> syncEvaluacionesAntropometricasFull(
            HttpServletRequest request
    ) {
        log.info("Solicitud de sincronización completa de evaluaciones antropométricas desde IP: {}", request.getRemoteAddr());
        
        try {
            FullSyncResponseDTO<EvaluacionAntropometricaDto> response = evaluacionAntropometricaService.findAllActive();

            log.info("Sincronización completa exitosa: {} evaluaciones antropométricas activos encontrados", response.getTotalRegistros());

            return ResponseEntity.ok(
                    ApiResponseDto.<FullSyncResponseDTO<EvaluacionAntropometricaDto>>builder()
                            .status(HttpStatus.OK.value())
                            .message("Lista de evaluaciones antropométricas recuperada exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(response)
                            .build()
            );

        } catch (Exception e) {
            log.error("Error durante la sincronización completa de evaluaciones antropométricas: {}", e.getMessage(), e);
            throw e;
        }
    }
}