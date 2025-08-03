package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.EnfermedadDto;
import com.nutrizulia.features.catalog.service.IEnfermedadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.CATALOG_API_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.ENFERMEDADES_ALL;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_API_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
)
public class EnfermedadController {

    private final IEnfermedadService enfermedadService;

    @Operation(
            summary = "Obtener todas las enfermedades",
            description = "Permite recuperar una lista de todas las enfermedades disponibles en el sistema. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de enfermedades recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnfermedadDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(ENFERMEDADES_ALL)
    public ResponseEntity<List<EnfermedadDto>> getEnfermedades() {
        return ResponseEntity.ok(enfermedadService.getEnfermedades());
    }

}
