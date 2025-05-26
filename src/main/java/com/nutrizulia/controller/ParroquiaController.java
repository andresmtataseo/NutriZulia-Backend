package com.nutrizulia.controller;

import com.nutrizulia.model.Municipio;
import com.nutrizulia.model.Parroquia;
import com.nutrizulia.service.IParroquiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
public class ParroquiaController {

    @Autowired
    private IParroquiaService parroquiaService;

    // Listar
    @Operation(summary = "Obtener lista de parroquias", description = "Devuelve una lista de parroquias opcionalmente filtradas por municipio, id o nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Parroquia.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("parroquias")
    public ResponseEntity<List<Parroquia>> getParroquias(
            @Parameter(description = "ID del municipio al que pertenece la parroquia", required = true, example = "1")
            @RequestParam Integer idMunicipio,
            @Parameter(description = "ID de la parroquia", example = "1")
            @RequestParam(required = false) Integer idParroquia,
            @Parameter(description = "Nombre de la parroquia", example = "San Francisco")
            @RequestParam(required = false) String nombre) {

        List<Parroquia> parroquias = parroquiaService.getParroquias(idMunicipio, idParroquia, nombre);
        return new ResponseEntity<>(parroquias, HttpStatus.OK);
    }

    // Crear
    @Operation(summary = "Crear una nueva parroquia", description = "Guarda una nueva parroquia en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parroquia creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Parroquia.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PostMapping("parroquias")
    public ResponseEntity<Parroquia> saveParroquia(@Valid @RequestBody Parroquia parroquia) {
        Parroquia nuevaParroquia = parroquiaService.saveParroquia(parroquia);
        return new ResponseEntity<>(nuevaParroquia, HttpStatus.CREATED);
    }

    // Actualizar
    @Operation(summary = "Actualizar una parroquia existente", description = "Actualiza los datos de una parroquia existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parroquia actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Parroquia.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parroquia no encontrada", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PutMapping("parroquias/{id}")
    public ResponseEntity<Parroquia> updateParroquia(
            @Parameter(description = "ID de la parroquia a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Parroquia parroquia) {

        if (!id.equals(parroquia.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Parroquia existente = parroquiaService.getParroquiaById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Parroquia parroquiaActualizada = parroquiaService.saveParroquia(parroquia);
        return ResponseEntity.ok(parroquiaActualizada);
    }

}
