package com.nutrizulia.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutrizulia.common.util.ApiConstants;

import java.io.IOException;

/**
 * Controlador para endpoints públicos.
 * 
 * Expone un endpoint para descargar el manual de la aplicación en formato PDF.
 * Ruta: GET /api/v1/public/manual-app
 */
@Slf4j
@RestController
@Tag(name = "Público", description = "Endpoints públicos que no requieren autenticación")
public class PublicController {

    @Operation(
            summary = "Descargar manual de la aplicación",
            description = "Descarga el archivo PDF 'manual-app.pdf' desde los recursos de la aplicación. " +
                    "Incluye cabeceras adecuadas para descarga de archivo (Content-Type y Content-Disposition)."
    )
    @SecurityRequirements({})
    @GetMapping(value = ApiConstants.PUBLIC_BASE_URL + ApiConstants.PUBLIC_MANUAL_APP, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadManualApp() {
        try {
            ClassPathResource resource = new ClassPathResource("manual-app.pdf");
            if (!resource.exists()) {
                log.warn("El recurso manual-app.pdf no existe en el classpath");
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"manual-app.pdf\"");
            headers.setContentType(MediaType.APPLICATION_PDF);

            long contentLength;
            try {
                contentLength = resource.contentLength();
                headers.setContentLength(contentLength);
            } catch (IOException e) {
                // Si no se puede determinar la longitud, se omite.
                log.debug("No se pudo determinar el tamaño del archivo manual-app.pdf: {}", e.getMessage());
            }

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception ex) {
            log.error("Error al servir el manual de la aplicación: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Descargar manual de la página web",
            description = "Descarga el archivo PDF 'manual-web.pdf' desde los recursos de la aplicación con cabeceras de descarga."
    )
    @SecurityRequirements({})
    @GetMapping(value = ApiConstants.PUBLIC_BASE_URL + ApiConstants.PUBLIC_MANUAL_WEB, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadManualWeb() {
        try {
            ClassPathResource resource = new ClassPathResource("manual-web.pdf");
            if (!resource.exists()) {
                log.warn("El recurso manual-web.pdf no existe en el classpath");
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"manual-web.pdf\"");
            headers.setContentType(MediaType.APPLICATION_PDF);

            try {
                headers.setContentLength(resource.contentLength());
            } catch (IOException e) {
                log.debug("No se pudo determinar el tamaño del archivo manual-web.pdf: {}", e.getMessage());
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception ex) {
            log.error("Error al servir el manual de la página web: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Descargar aplicación móvil (APK)",
            description = "Descarga el archivo 'nutrizulia.apk' desde los recursos de la aplicación."
    )
    @SecurityRequirements({})
    @GetMapping(value = ApiConstants.PUBLIC_BASE_URL + ApiConstants.PUBLIC_APK, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadApk() {
        try {
            ClassPathResource resource = new ClassPathResource("nutrizulia.apk");
            if (!resource.exists()) {
                log.warn("El recurso nutrizulia.apk no existe en el classpath");
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"nutrizulia.apk\"");
            headers.setContentType(MediaType.parseMediaType("application/vnd.android.package-archive"));

            try {
                headers.setContentLength(resource.contentLength());
            } catch (IOException e) {
                log.debug("No se pudo determinar el tamaño del archivo nutrizulia.apk: {}", e.getMessage());
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception ex) {
            log.error("Error al servir el APK de la aplicación: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}