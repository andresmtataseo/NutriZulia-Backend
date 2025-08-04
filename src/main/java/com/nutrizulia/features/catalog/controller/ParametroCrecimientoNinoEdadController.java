package com.nutrizulia.features.catalog.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.catalog.dto.ParametroCrecimientoNinoEdadDto;
import com.nutrizulia.features.catalog.service.IParametroCrecimientoNinoEdadService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_BASE_URL)
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

    @GetMapping(CATALOG_CHILDREN_AGE_PARAMETERS)
    public ResponseEntity<ApiResponseDto<List<ParametroCrecimientoNinoEdadDto>>> getParametrosCrecimientosNinosEdad(HttpServletRequest request) {
        List<ParametroCrecimientoNinoEdadDto> parametros = parametroCrecimientoNinoEdadService.getParametrosCrecimientosNinosEdad();
        
        ApiResponseDto<List<ParametroCrecimientoNinoEdadDto>> response = ApiResponseDto.<List<ParametroCrecimientoNinoEdadDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Lista de parámetros de crecimiento de niños por edad recuperada exitosamente")
                .data(parametros)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }

}
