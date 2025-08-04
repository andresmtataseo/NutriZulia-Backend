package com.nutrizulia.features.collection.controller;

import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import com.nutrizulia.common.dto.ApiResponseDto;
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
import static com.nutrizulia.common.util.ApiConstants.COLLECTION_SYNC_PATIENT_REPRESENTATIVES;

@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<ApiResponseDto<List<PacienteRepresentanteDto>>> syncPacienteRepresentantes(
            @RequestBody List<PacienteRepresentanteDto> pacienteRepresentantes, HttpServletRequest request) {

        List<PacienteRepresentanteDto> pacienteRepresentantesDesdeServidor = pacienteRepresentanteService.syncPacientesRepresentantes(pacienteRepresentantes);

        return ResponseEntity.ok(
                ApiResponseDto.<List<PacienteRepresentanteDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Sincronización de relaciones paciente-representante completada")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(pacienteRepresentantesDesdeServidor)
                        .build()
        );
    }
}