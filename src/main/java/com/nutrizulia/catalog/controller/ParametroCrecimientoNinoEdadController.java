package com.nutrizulia.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.catalog.dto.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.catalog.service.IParametroCrecimientoNinoEdadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.CATALOG_API_BASE_URL;
import static com.nutrizulia.common.util.ApiConstants.PARAMETROS_NINOS_EDAD_ALL;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_API_BASE_URL)
@Tag(
        name = "Catálogos del Sistema",
        description = "Consulta de catálogos predefinidos como etnias, grupos etarios, parroquias, entre otros."
)
public class ParametroCrecimientoNinoEdadController {

    private final IParametroCrecimientoNinoEdadService parametroCrecimientoNinoEdadService;

    @Operation(summary = "Obtener todos los parametros de crecimiento de los niños", description = "Permite recuperar una lista de todos los parametros de crecimientos de niños de 5-19 años segun la OMS. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParametroCrecimientoNinoEdadDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })

    @GetMapping(PARAMETROS_NINOS_EDAD_ALL)
    public ResponseEntity<List<ParametroCrecimientoNinoEdadDto>> getParametrosCrecimientosNinosEdad() {
        return ResponseEntity.ok(parametroCrecimientoNinoEdadService.getParametrosCrecimientosNinosEdad());
    }

}
