package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.PacienteDto;
import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
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

import static com.nutrizulia.common.util.ApiConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(COLLECTION_BASE_URL)
@Tag(
        name = "Recolección de Datos",
        description = "Endpoints para recibir, consultar y procesar los datos recolectados desde la aplicación móvil, como actividades realizadas, pacientes, consultas y otros registros clínicos."
)
public class PacienteRepresentanteController {

    private final IPacienteRepresentanteService pacienteRepresentanteService;

    @Operation(summary = "Sincronizar lista de relaciones paciente-representante", description = "Recibe y sincroniza una lista de relaciones entre pacientes y representantes desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relaciones paciente-representante sincronizadas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_PATIENT_REPRESENTATIVES)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncPacienteRepresentantes(
            @RequestBody @Validated List<PacienteRepresentanteDto> pacienteRepresentantes, HttpServletRequest request) {

        try {
            log.info("Iniciando sincronización de {} relaciones paciente-representante", pacienteRepresentantes.size());
            
            if (pacienteRepresentantes.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("La lista de relaciones paciente-representante no puede estar vacía")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
                );
            }
            
            BatchSyncResponseDTO syncResponse = pacienteRepresentanteService.syncPacientesRepresentantes(pacienteRepresentantes);
            HttpStatus status = determinarEstadoRespuesta(syncResponse);
            String mensaje = construirMensajeRespuesta(syncResponse, "relaciones paciente-representante");
            
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
            log.error("Error durante la sincronización de relaciones paciente-representante: {}", e.getMessage(), e);
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

    @Operation(summary = "Obtener todas las relaciones paciente-representante activas",
            description = "Obtiene todas las relaciones paciente-representante activas filtradas por las instituciones del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relaciones obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullSyncResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(COLLECTION_SYNC_PATIENT_REPRESENTATIVES_FULL)
    public ResponseEntity<ApiResponseDto<FullSyncResponseDTO<PacienteRepresentanteDto>>> getAllActivePacienteRepresentantes(
            HttpServletRequest request
    ) {

        log.info("Solicitud de sincronización completa de pacientes-representantes desde IP: {}", request.getRemoteAddr());

        try {
            FullSyncResponseDTO<PacienteRepresentanteDto> response = pacienteRepresentanteService.findAllActive();

            log.info("Sincronización completa exitosa: {} pacientes-representantes activos encontrados", response.getTotalRegistros());

            return ResponseEntity.ok(
                    ApiResponseDto.<FullSyncResponseDTO<PacienteRepresentanteDto>>builder()
                            .status(HttpStatus.OK.value())
                            .message("Lista de relaciones paciente-representante recuperada exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(response)
                            .build()
            );

        } catch (Exception e) {
            log.error("Error durante la sincronización completa de pacientes-representantes: {}", e.getMessage(), e);
            throw e;
        }

    }
}