package com.nutrizulia.features.reports.controller;

import com.nutrizulia.features.reports.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/report")
@Tag(
        name = "Reportes del Sistema",
        description = "Genera los reportes del sistema"
)
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(
            summary = "Generar reporte anual de instituciones",
            description = "**Requiere autenticación.** Genera un reporte Excel ANUAL con las instituciones del municipio sanitario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de municipio sanitario inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron instituciones para el municipio sanitario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/annual")
    public ResponseEntity<ByteArrayResource> generarReporteAnual(
            @Parameter(description = "ID del municipio sanitario", required = true)
            @RequestParam Integer municipioSanitarioId,
            @Parameter(description = "Año del reporte (ej. 2025)", required = true)
            @RequestParam Integer anio,
            HttpServletRequest request) {

        try {
            log.info("Solicitud de reporte anual para municipio sanitario ID: {} y año: {}", municipioSanitarioId, anio);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reporteService.generarReporteAnualPorMunicipio(municipioSanitarioId, anio, outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("reporte_anual_municipio_%d_%d_%s.xlsx", municipioSanitarioId, anio, timestamp);

            log.info("Reporte anual generado exitosamente: {}", filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al generar reporte anual para municipio sanitario ID {} y año {}: {}", 
                    municipioSanitarioId, anio, e.getMessage(), e);
            throw new RuntimeException("Error interno al generar el reporte", e);
        }
    }

    @Operation(
            summary = "Generar reporte trimestral de instituciones",
            description = "**Requiere autenticación.** Genera un reporte Excel TRIMESTRAL con las instituciones del municipio sanitario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontraron instituciones para el municipio sanitario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/quarterly")
    public ResponseEntity<ByteArrayResource> generarReporteTrimestral(
            @Parameter(description = "ID del municipio sanitario", required = true)
            @RequestParam Integer municipioSanitarioId,
            @Parameter(description = "Año del reporte (ej. 2025)", required = true)
            @RequestParam Integer anio,
            @Parameter(description = "Trimestre del año [1..4]", required = true)
            @RequestParam Integer trimestre,
            HttpServletRequest request) {

        try {
            log.info("Solicitud de reporte trimestral T{} para municipio sanitario ID: {} y año: {}", trimestre, municipioSanitarioId, anio);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reporteService.generarReporteTrimestralPorMunicipio(municipioSanitarioId, anio, trimestre, outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("reporte_trimestral_municipio_%d_%d_T%d_%s.xlsx", municipioSanitarioId, anio, trimestre, timestamp);

            log.info("Reporte trimestral generado exitosamente: {}", filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al generar reporte trimestral T{} para municipio sanitario ID {} y año {}: {}", trimestre, municipioSanitarioId, anio, e.getMessage(), e);
            throw new RuntimeException("Error interno al generar el reporte", e);
        }
    }

    @Operation(
            summary = "Frescura de datos por municipio (JSON)",
            description = "**Requiere autenticación.** Retorna la última actualización por usuario activo en cada institución del municipio sanitario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de municipio sanitario inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/data-freshness")
    public ResponseEntity<?> obtenerDataFreshness(
            @Parameter(description = "ID del municipio sanitario", required = true)
            @RequestParam Integer municipioSanitarioId) {
        try {
            log.info("Solicitud de data freshness para municipio sanitario ID: {}", municipioSanitarioId);
            return ResponseEntity.ok(reporteService.obtenerDataFreshnessPorMunicipio(municipioSanitarioId));
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener data freshness para municipio sanitario ID {}: {}", municipioSanitarioId, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener data freshness", e);
        }
    }

}
