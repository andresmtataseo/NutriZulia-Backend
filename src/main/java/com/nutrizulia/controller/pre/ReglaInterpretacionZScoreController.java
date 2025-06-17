package com.nutrizulia.controller.pre;

import com.nutrizulia.dto.error.ErrorResponse;
import com.nutrizulia.dto.pre.ReglaInterpretacionPercentilDto;
import com.nutrizulia.dto.pre.ReglaInterpretacionZScoreDto;
import com.nutrizulia.model.pre.ReglaInterpretacionZScore;
import com.nutrizulia.service.pre.IReglaInterpretacionPercentilService;
import com.nutrizulia.service.pre.IReglaInterpretacionZScoreService;
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

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Datos precargados", description = "API de datos precargados")
@SecurityRequirement(name = "bearerAuth")
public class ReglaInterpretacionZScoreController {

    private final IReglaInterpretacionZScoreService reglaInterpretacionZScoreService;

    @Operation(summary = "Obtener todas las reglas para la interpretación de resultados basados en Z-Scores.", description = "Permite recuperar una lista de todas las reglas para la interpretación de resultados basados en Z-Scores, según los estándares de crecimiento de la OMS. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReglaInterpretacionZScoreDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping("v1/reglasInterpretacionesZScore")
    public ResponseEntity<List<ReglaInterpretacionZScoreDto>> getReglasInterpretacionesZScore() {
        return ResponseEntity.ok(reglaInterpretacionZScoreService.getReglasInterpretacionesZScore());
    }

}
