package com.nutrizulia.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Salud del Servicio", description = "Chequeo simple del estado del servidor para plataformas como Render.")
public class HealthController {

    @Operation(summary = "Estado del servidor", description = "Devuelve 200 OK con información básica si el servidor está operativo.")
    @SecurityRequirements({})
    @GetMapping("/healt")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "UP");
        body.put("timestamp", Instant.now().toString());
        body.put("service", "nutrizulia-backend");
        return ResponseEntity.ok(body);
    }
}