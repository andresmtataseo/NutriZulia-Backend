package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.service.IDetalleVitalService;
import com.nutrizulia.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.COLLECTION_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_VITAL_DETAILS;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class DetalleVitalController {

    private final IDetalleVitalService detalleVitalService;

    @Operation(summary = "Sincronizar lista de detalles vitales", description = "Recibe y sincroniza una lista de detalles vitales desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles vitales sincronizados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_VITAL_DETAILS)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncDetallesVitales(
            @Valid @RequestBody List<DetalleVitalDto> detallesVitales, HttpServletRequest request) {
        
        try {
            log.info("Iniciando sincronización de {} detalles vitales", detallesVitales.size());
            
            if (detallesVitales.isEmpty()) {
                log.warn("Lista de detalles vitales vacía recibida");
                return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("La lista de detalles vitales no puede estar vacía")
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            }
            
            BatchSyncResponseDTO resultado = detalleVitalService.syncDetallesVital(detallesVitales);
            HttpStatus estadoRespuesta = determinarEstadoRespuesta(resultado);
            String mensajeRespuesta = construirMensajeRespuesta(resultado, "detalles vitales");
            
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
            log.error("Error inesperado durante la sincronización de detalles vitales", e);
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
    
    @Operation(summary = "Obtener todos los detalles vitales activos", description = "Obtiene todos los detalles vitales activos para sincronización completa. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles vitales obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping("/sync/detalles-vitales/full")
    public ResponseEntity<FullSyncResponseDTO<DetalleVitalDto>> getAllActiveDetallesVitales(HttpServletRequest request) {

        log.info("Solicitud de sincronización completa de detalles vitales desde IP: {}", request.getRemoteAddr());

        try {
            FullSyncResponseDTO<DetalleVitalDto> response = detalleVitalService.findAllActive();

            log.info("Sincronización completa exitosa: {} detalles vitales activos encontrados", response.getTotalRegistros());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error durante la sincronización completa de detalles vitales: {}", e.getMessage(), e);
            throw e;
        }

    }
}