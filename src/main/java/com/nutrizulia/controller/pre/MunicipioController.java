package com.nutrizulia.controller.pre;

import com.nutrizulia.dto.error.ApiResponseDto;
import com.nutrizulia.dto.pre.MunicipioDto;
import com.nutrizulia.service.pre.IMunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Datos precargados", description = "API de datos precargados")
@SecurityRequirement(name = "bearerAuth")
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
    @GetMapping("v1/municipios")
    public ResponseEntity<List<MunicipioDto>> getMunicipios( @Valid
            @Parameter(description = "ID del estado al cual pertenecen los municipios", required = true, example = "23")
            @RequestParam Integer idEstado) {

        List<MunicipioDto> municipios = municipioService.getMunicipios(idEstado);
        return ResponseEntity.ok(municipios);
    }

}
