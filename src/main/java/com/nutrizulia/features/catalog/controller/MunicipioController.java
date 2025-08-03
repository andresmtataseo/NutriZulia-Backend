package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.MunicipioDto;
import com.nutrizulia.features.catalog.service.IMunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.CATALOG_API_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.MUNICIPIOS_ALL;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_API_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
)
public class MunicipioController {

    private final IMunicipioService municipioService;

    @Operation(
            summary = "Obtener lista de municipios por estado",
            description = "Devuelve una lista de los municipios por estado. **Requiere autenticación.**"
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MunicipioDto.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado - El recurso solicitado no existe.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(MUNICIPIOS_ALL)
    public ResponseEntity<List<MunicipioDto>> getMunicipios( @Valid
            @Parameter(description = "ID del estado al cual pertenecen los municipios", required = true, example = "23")
            @RequestParam Integer idEstado) {

        List<MunicipioDto> municipios = municipioService.getMunicipios(idEstado);
        return ResponseEntity.ok(municipios);
    }

}
