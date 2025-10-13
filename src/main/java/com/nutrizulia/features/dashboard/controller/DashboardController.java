package com.nutrizulia.features.dashboard.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.dashboard.dto.ChartResponseDto;
import com.nutrizulia.features.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import static com.nutrizulia.common.util.ApiConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(DASHBOARD_BASE_URL)
@Tag(
        name = "Dashboard",
        description = "Endpoints para gráficos y métricas del sistema NutriZulia"
)
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Consultas por mes",
            description = "Devuelve el total de consultas agrupadas por mes en un rango dado. Permite filtrar por municipio e institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gráfico obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChartResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(DASHBOARD_CONSULTATIONS_PER_MONTH)
    public ResponseEntity<ApiResponseDto<ChartResponseDto>> getConsultasPorMes(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(required = false) Integer municipioId,
            @RequestParam(required = false) Integer institucionId,
            HttpServletRequest request
    ) {
        try {
            YearMonth ymInicio = parseYearMonth(inicio, "inicio");
            YearMonth ymFin = parseYearMonth(fin, "fin");

            ChartResponseDto chart = dashboardService.getConsultasPorMes(ymInicio, ymFin, municipioId, institucionId);

            return ResponseEntity.ok(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.OK.value())
                            .message("Gráfico de consultas por mes recuperado exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(chart)
                            .build()
            );
        } catch (IllegalArgumentException ex) {
            log.warn("Parámetros inválidos en consultas por mes: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(ex.getMessage())
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }

    @Operation(summary = "Instituciones activas por municipio",
            description = "Devuelve el conteo actual de instituciones activas agrupadas por municipio. Permite filtrar por municipio sanitario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gráfico obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChartResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(DASHBOARD_ACTIVE_INSTITUTIONS_BY_MUNICIPALITY)
    public ResponseEntity<ApiResponseDto<ChartResponseDto>> getInstitucionesActivasPorMunicipio(
            @RequestParam(required = false) Integer municipioId,
            HttpServletRequest request
    ) {
        ChartResponseDto chart = dashboardService.getInstitucionesActivasPorMunicipio(municipioId);
        return ResponseEntity.ok(
                ApiResponseDto.<ChartResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Gráfico de instituciones activas por municipio recuperado exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(chart)
                        .build()
        );
    }

    @Operation(summary = "Usuarios activos por institución",
            description = "Devuelve el conteo actual de usuarios activos agrupados por institución. Permite filtrar por municipio sanitario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gráfico obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChartResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(DASHBOARD_ACTIVE_USERS_BY_INSTITUTION)
    public ResponseEntity<ApiResponseDto<ChartResponseDto>> getUsuariosActivosPorInstitucion(
        @RequestParam(required = false) Integer municipioId,
        HttpServletRequest request
    ) {
        ChartResponseDto chart = dashboardService.getUsuariosActivosPorInstitucion(municipioId);
        return ResponseEntity.ok(
                ApiResponseDto.<ChartResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Gráfico de usuarios activos por institución recuperado exitosamente")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .data(chart)
                        .build()
        );
    }


    @Operation(summary = "Distribución por grupo etario",
            description = "Devuelve la distribución de pacientes por grupo etario en un rango de fechas. Permite filtrar por institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gráfico obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChartResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(DASHBOARD_AGE_GROUP_DISTRIBUTION)
    public ResponseEntity<ApiResponseDto<ChartResponseDto>> getDistribucionGrupoEtario(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(required = false) Integer institucionId,
            @RequestParam(required = false) Integer municipioId,
            HttpServletRequest request
    ) {
        try {
            YearMonth ymInicio = parseYearMonth(inicio, "inicio");
            YearMonth ymFin = parseYearMonth(fin, "fin");

            ChartResponseDto chart = dashboardService.getDistribucionGrupoEtario(ymInicio, ymFin, institucionId, municipioId);

            return ResponseEntity.ok(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.OK.value())
                            .message("Gráfico de distribución por grupo etario recuperado exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(chart)
                            .build()
            );
        } catch (IllegalArgumentException ex) {
            log.warn("Parámetros inválidos en distribución por grupo etario: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(ex.getMessage())
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }

    @Operation(summary = "Estado nutricional por grupo etario",
            description = "Devuelve el estado nutricional por grupo etario en un rango de fechas. Permite filtrar por institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gráfico obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChartResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(DASHBOARD_NUTRITIONAL_STATUS_BY_AGE_GROUP)
    public ResponseEntity<ApiResponseDto<ChartResponseDto>> getEstadoNutricionalPorGrupoEtario(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(required = false) Integer institucionId,
            @RequestParam(required = false) Integer municipioId,
            HttpServletRequest request
    ) {
        try {
            YearMonth ymInicio = parseYearMonth(inicio, "inicio");
            YearMonth ymFin = parseYearMonth(fin, "fin");

            ChartResponseDto chart = dashboardService.getEstadoNutricionalPorGrupoEtario(ymInicio, ymFin, institucionId, municipioId);

            return ResponseEntity.ok(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.OK.value())
                            .message("Gráfico de estado nutricional por grupo etario recuperado exitosamente")
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .data(chart)
                            .build()
            );
        } catch (IllegalArgumentException ex) {
            log.warn("Parámetros inválidos en estado nutricional por grupo etario: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<ChartResponseDto>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(ex.getMessage())
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
        }
    }

    private YearMonth parseYearMonth(String value, String paramName) {
        try {
            return YearMonth.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Parámetro '" + paramName + "' inválido. Formato esperado: YYYY-MM");
        }
    }
}