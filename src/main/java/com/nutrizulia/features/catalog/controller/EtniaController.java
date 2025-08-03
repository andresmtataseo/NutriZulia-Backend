package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.EtniaDto;
import com.nutrizulia.features.catalog.service.IEtniaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.CATALOG_API_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.ETNIAS_ALL;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_API_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
)
public class EtniaController {

    private final IEtniaService etniaService;

    @Operation(
            summary = "Obtener todas las etnias",
            description = "Permite recuperar una lista de todas las etnias disponibles en el sistema. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de etnias recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EtniaDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })

    @GetMapping(ETNIAS_ALL)
    public ResponseEntity<List<EtniaDto>> getEtnias() {
        return ResponseEntity.ok(etniaService.getEtnias());
    }
}