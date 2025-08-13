package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteDto;
import com.nutrizulia.features.collection.service.IPacienteService;
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

import static com.nutrizulia.common.util.ApiConstants.COLLECTION_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_PATIENTS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(COLLECTION_BASE_URL)
@Validated
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class PacienteController {

    private final IPacienteService pacienteService;

    @Operation(summary = "Sincronizar lista de pacientes", description = "Recibe y sincroniza una lista de pacientes desde la aplicación móvil. Devuelve los IDs de pacientes sincronizados exitosamente y los que fallaron con sus respectivas razones. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sincronización completada (puede incluir éxitos y fallos)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Todos los pacientes fallaron en la sincronización", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_PATIENTS)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncPacientes(
            @RequestBody @Validated List<PacienteDto> pacientes, HttpServletRequest request) {
        
        try {
            log.info("Iniciando sincronización de {} pacientes", pacientes.size());
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("La lista de pacientes no puede estar vacía")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
                );
            }
            
            BatchSyncResponseDTO syncResult = pacienteService.syncPacientes(pacientes);
            
            HttpStatus responseStatus = determinarEstadoRespuesta(syncResult);
            String mensaje = construirMensajeRespuesta(syncResult);
            
            log.info("Sincronización completada: {} éxitos, {} fallos", 
                    syncResult.getSuccess().size(), syncResult.getFailed().size());
            
            return ResponseEntity.status(responseStatus).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(responseStatus.value())
                            .message(mensaje)
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(syncResult)
                            .build()
            );
            
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos en la sincronización: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Datos inválidos: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error inesperado durante la sincronización de pacientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error interno del servidor")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }
    
    private HttpStatus determinarEstadoRespuesta(BatchSyncResponseDTO syncResult) {
        boolean tieneExitos = !syncResult.getSuccess().isEmpty();
        boolean tieneFallos = !syncResult.getFailed().isEmpty();
        
        if (tieneExitos && !tieneFallos) {
            return HttpStatus.OK; // Todos exitosos
        } else if (tieneExitos && tieneFallos) {
            return HttpStatus.OK; // Sincronización parcial - consideramos OK porque algunos fueron exitosos
        } else if (!tieneExitos && tieneFallos) {
            return HttpStatus.CONFLICT; // Todos fallaron
        } else {
            return HttpStatus.OK; // Lista vacía - caso edge
        }
    }
    
    private String construirMensajeRespuesta(BatchSyncResponseDTO syncResult) {
        int exitosos = syncResult.getSuccess().size();
        int fallidos = syncResult.getFailed().size();
        
        if (exitosos > 0 && fallidos == 0) {
            return String.format("Sincronización completada exitosamente: %d pacientes sincronizados", exitosos);
        } else if (exitosos > 0 && fallidos > 0) {
            return String.format("Sincronización parcial: %d exitosos, %d fallidos", exitosos, fallidos);
        } else if (exitosos == 0 && fallidos > 0) {
            return String.format("Sincronización fallida: %d pacientes no pudieron ser sincronizados", fallidos);
        } else {
            return "Sincronización completada sin cambios";
        }
    }

}
