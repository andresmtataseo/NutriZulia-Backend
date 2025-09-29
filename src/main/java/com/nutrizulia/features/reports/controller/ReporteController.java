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

import static com.nutrizulia.common.util.ApiConstants.CATALOG_BASE_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(CATALOG_BASE_URL)
@Tag(
        name = "Reportes del Sistema",
        description = "Genera los reportes del sistema"
)
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(
            summary = "Generar reporte trimestral de instituciones",
            description = "**Requiere autenticación.** Genera un reporte Excel con las instituciones del municipio sanitario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de municipio sanitario inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron instituciones para el municipio sanitario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporte-trimestral")
    public ResponseEntity<ByteArrayResource> generarReporteTrimestral(
            @Parameter(description = "ID del municipio sanitario", required = true)
            @RequestParam Integer municipioSanitarioId,
            HttpServletRequest request) {

        try {
            log.info("Solicitud de reporte trimestral para municipio sanitario ID: {}", municipioSanitarioId);

            // Generar el reporte usando ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reporteService.generarReporteTrimestralPorMunicipio(municipioSanitarioId, outputStream);

            // Crear el recurso de bytes
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            // Generar nombre del archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("reporte_trimestral_municipio_%d_%s.xlsx", municipioSanitarioId, timestamp);

            log.info("Reporte generado exitosamente: {}", filename);

            // Configurar headers para descarga
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al generar reporte trimestral para municipio sanitario ID {}: {}", 
                    municipioSanitarioId, e.getMessage(), e);
            throw new RuntimeException("Error interno al generar el reporte", e);
        }
    }
}
