package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;
import com.nutrizulia.features.collection.service.IDetallePediatricoService;
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

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.COLLECTION_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_PEDIATRIC_DETAILS;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class DetallePediatricoController {

    private final IDetallePediatricoService detallePediatricoService;

    @Operation(summary = "Sincronizar lista de detalles pediátricos", description = "Recibe y sincroniza una lista de detalles pediátricos desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles pediátricos sincronizados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_PEDIATRIC_DETAILS)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncDetallesPediatricos(
            @Valid @RequestBody List<DetallePedriatricoDto> detallesPediatricos, HttpServletRequest request) {
        
        try {
            log.info("Iniciando sincronización de {} detalles pediátricos", detallesPediatricos.size());
            
            if (detallesPediatricos.isEmpty()) {
                log.warn("Lista de detalles pediátricos vacía recibida");
                return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("La lista de detalles pediátricos no puede estar vacía")
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            }
            
            BatchSyncResponseDTO resultado = detallePediatricoService.syncDetallesPediatrico(detallesPediatricos);
            HttpStatus estadoRespuesta = determinarEstadoRespuesta(resultado);
            String mensajeRespuesta = construirMensajeRespuesta(resultado, "detalles pediátricos");
            
            log.info("Sincronización completada - Éxitos: {}, Fallos: {}", 
                resultado.getSuccess().size(), resultado.getFailed().size());
            
            return ResponseEntity.status(estadoRespuesta).body(
                ApiResponseDto.<BatchSyncResponseDTO>builder()
                    .status(estadoRespuesta.value())
                    .message(mensajeRespuesta)
                    .timestamp(LocalDateTime.now())
                    .data(resultado)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error inesperado durante la sincronización de detalles pediátricos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseDto.<BatchSyncResponseDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error interno del servidor durante la sincronización")
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        }
    }
    
    private HttpStatus determinarEstadoRespuesta(BatchSyncResponseDTO resultado) {
        if (resultado.getFailed().isEmpty()) {
            return HttpStatus.OK;
        } else if (resultado.getSuccess().isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.PARTIAL_CONTENT;
        }
    }
    
    private String construirMensajeRespuesta(BatchSyncResponseDTO resultado, String tipoEntidad) {
        int exitosos = resultado.getSuccess().size();
        int fallidos = resultado.getFailed().size();
        
        if (fallidos == 0) {
            return String.format("Todos los %s (%d) fueron sincronizados exitosamente", tipoEntidad, exitosos);
        } else if (exitosos == 0) {
            return String.format("Ningún %s pudo ser sincronizado (%d fallos)", tipoEntidad.substring(0, tipoEntidad.length()-1), fallidos);
        } else {
            return String.format("Sincronización parcial: %d %s exitosos, %d fallos", exitosos, tipoEntidad, fallidos);
        }
    }
}