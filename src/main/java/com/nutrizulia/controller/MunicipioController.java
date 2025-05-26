package com.nutrizulia.controller;

import com.nutrizulia.model.Municipio;
import com.nutrizulia.service.IMunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
public class MunicipioController {

    @Autowired
    private IMunicipioService municipioService;

    // Listar
    @Operation(summary = "Obtener municipios por estado y otros filtros opcionales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Municipios obtenidos correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Municipio.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos")
    })
    @GetMapping("municipios")
    public ResponseEntity<List<Municipio>> getMunicipios(
            @Parameter(description = "ID del estado al cual pertenecen los municipios", required = true, example = "23")
            @RequestParam(required = true) Integer idEstado,

            @Parameter(description = "ID específico del municipio a buscar", example = "4")
            @RequestParam(required = false) Integer idMunicipio,

            @Parameter(description = "Nombre del municipio a buscar", example = "Maracaibo")
            @RequestParam(required = false) String nombre) {

        List<Municipio> municipios = municipioService.getMunicipios(idEstado, idMunicipio, nombre);
        return ResponseEntity.ok(municipios);
    }


    // crear
    @Operation(summary = "Crear un nuevo municipio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Municipio creado exitosamente", content = @Content(schema = @Schema(implementation = Municipio.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PostMapping("municipios")
    public ResponseEntity<Municipio> createMunicipio(
            @Valid @RequestBody Municipio municipio) {
        municipio.setId(null);
        Municipio nuevoMunicipio = municipioService.saveMunicipio(municipio);
        return new ResponseEntity<>(nuevoMunicipio, HttpStatus.CREATED);
    }

    // actualizar
    @Operation(summary = "Actualizar un municipio existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Municipio actualizado exitosamente", content = @Content(schema = @Schema(implementation = Municipio.class))),
            @ApiResponse(responseCode = "404", description = "Municipio no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN_WEB')")
    @PutMapping("municipios/{id}")
    public ResponseEntity<Municipio> updateMunicipio(
            @Parameter(description = "ID del municipio a actualizar")
            @PathVariable Integer id,

            @Valid @RequestBody Municipio municipio) {

        if (!id.equals(municipio.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Municipio existente = municipioService.getMunicipioById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Municipio actualizado = municipioService.saveMunicipio(municipio);
        return ResponseEntity.ok(actualizado);
    }
}
