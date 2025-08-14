package com.nutrizulia.features.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta para sincronización completa de una tabla")
public class FullSyncResponseDTO<T> {

    @Schema(description = "Nombre de la tabla sincronizada", example = "pacientes")
    private String tabla;

    @Schema(description = "Número total de registros devueltos", example = "150")
    private int totalRegistros;

    @Schema(description = "Lista de datos de la tabla")
    private List<T> datos;
}