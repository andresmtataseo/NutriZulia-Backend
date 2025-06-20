package com.nutrizulia.controller.pre;

import com.nutrizulia.dto.error.ApiResponseDto;
import com.nutrizulia.dto.pre.EnfermedadDto;
import com.nutrizulia.service.pre.IEnfermedadService;
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
@RequiredArgsConstructor
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Datos precargados", description = "API de datos precargados")
@SecurityRequirement(name = "bearerAuth")
public class EnfermedadController {

    private final IEnfermedadService enfermedadService;

    @Operation(summary = "Obtener todas las enfermedades", description = "Permite recuperar una lista de todas las enfermedades disponibles en el sistema. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de enfermedades recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnfermedadDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping("v1/enfermedades")
    public ResponseEntity<List<EnfermedadDto>> getEnfermedades() {
        return ResponseEntity.ok(enfermedadService.getEnfermedades());
    }

}
