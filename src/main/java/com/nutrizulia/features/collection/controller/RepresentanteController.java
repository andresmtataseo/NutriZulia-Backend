package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.RepresentanteDto;
import com.nutrizulia.features.collection.service.IRepresentanteService;
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
public class RepresentanteController {

    private final IRepresentanteService representanteService;

    @Operation(summary = "Sincronizar lista de representantes", description = "Recibe y sincroniza una lista de representantes desde la aplicación móvil. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Representantes sincronizados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(COLLECTION_SYNC_REPRESENTATIVES)
    public ResponseEntity<ApiResponseDto<BatchSyncResponseDTO>> syncRepresentantes(
            @RequestBody @Validated List<RepresentanteDto> representantes, HttpServletRequest request) {

        try {
            log.info("Iniciando sincronización de {} representantes", representantes.size());
            
            if (representantes.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    ApiResponseDto.<BatchSyncResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("La lista de representantes no puede estar vacía")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
                );
            }
            
            BatchSyncResponseDTO syncResponse = representanteService.syncRepresentantes(representantes);
            HttpStatus status = determinarEstadoRespuesta(syncResponse);
            String mensaje = construirMensajeRespuesta(syncResponse, "representantes");
            
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
            log.error("Error durante la sincronización de representantes: {}", e.getMessage(), e);
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
    
    @Operation(
            summary = "Sincronización completa de representantes",
            description = "Obtiene todos los representantes activos filtrados por las instituciones activas del usuario autenticado. **Requiere autenticación JWT.**"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sincronización completa exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FullSyncResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado - Token JWT inválido o expirado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido - Sin permisos suficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class)
                    )
            )
    })
    @GetMapping(COLLECTION_SYNC_REPRESENTATIVES_FULL)
    public ResponseEntity<ApiResponseDto<FullSyncResponseDTO<RepresentanteDto>>> getAllActiveRepresentantes(
            HttpServletRequest request
    ) {
        log.info("Solicitud de sincronización completa de representantes desde IP: {}", request.getRemoteAddr());
        
        try {
            FullSyncResponseDTO<RepresentanteDto> response = representanteService.findAllActive();
            
            log.info("Sincronización completa exitosa: {} representantes activos encontrados", response.getTotalRegistros());
            
            return ResponseEntity.ok(
                    ApiResponseDto.<FullSyncResponseDTO<RepresentanteDto>>builder()
                            .status(HttpStatus.OK.value())
                            .message("Lista de representantes recuperada exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(response)
                            .build()
            );
            
        } catch (Exception e) {
            log.error("Error durante la sincronización completa de representantes: {}", e.getMessage(), e);
            throw e;
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