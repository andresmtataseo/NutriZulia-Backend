package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.ReglaInterpretacionPercentilDto;
import com.nutrizulia.features.catalog.service.IReglaInterpretacionPercentilService;
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

import static com.nutrizulia.common.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
)
public class ReglaInterpretacionPercentilController {

    private final IReglaInterpretacionPercentilService reglaInterpretacionPercentilService;

    @Operation(summary = "Obtener todas las reglas para la interpretación de resultados basados en percentiles.", description = "Permite recuperar una lista de todas las reglas para la interpretación de resultados basados en percentiles, según los estándares de crecimiento. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReglaInterpretacionPercentilDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })

    @GetMapping(CATALOG_PERCENTILE_INTERPRETATION_RULES)
    public ResponseEntity<List<ReglaInterpretacionPercentilDto>> getReglasInterpretacionesPercentil() {
        return ResponseEntity.ok(reglaInterpretacionPercentilService.getReglasInterpretacionesPercentil());
    }

}
