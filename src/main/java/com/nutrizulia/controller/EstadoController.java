package com.nutrizulia.controller;

import com.nutrizulia.model.Estado;
import com.nutrizulia.service.IEstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
public class EstadoController {

    @Autowired
    private IEstadoService estadoService;

    // Listar
    @Operation(
            summary = "Obtener lista de estados",
            description = "Devuelve una lista de estados opcionalmente filtrados por ID o nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("estados")
    public ResponseEntity<List<Estado>> getEstados(
            @Parameter(description = "ID del estado a buscar", example = "1")
            @RequestParam(required = false) Integer idEstado,

            @Parameter(description = "Nombre del estado a buscar", example = "Zulia")
            @RequestParam(required = false) String nombreEstado) {

        List<Estado> estados = estadoService.getEstados(idEstado, nombreEstado);
        return ResponseEntity.ok(estados);
    }

    // Crear
    @Operation(
            summary = "Crear un nuevo estado",
            description = "Permite crear un nuevo estado enviando un objeto Estado válido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PostMapping("estados")
    public ResponseEntity<Estado> crearEstado(
            @Valid @RequestBody Estado estado) {
        Estado nuevoEstado = estadoService.saveEstado(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    // Actualizar
    @Operation(
            summary = "Actualizar un estado existente",
            description = "Permite actualizar un estado existente por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PutMapping("estados/{id}")
    public ResponseEntity<Estado> actualizarEstado(
            @Parameter(description = "ID del estado a actualizar", example = "1", required = true)
            @PathVariable Integer id,

            @Valid @RequestBody Estado estadoActualizado) {

        Estado existente = estadoService.getEstadoById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setNombre(estadoActualizado.getNombre());

        Estado actualizado = estadoService.saveEstado(existente);
        return ResponseEntity.ok(actualizado);
    }

}
