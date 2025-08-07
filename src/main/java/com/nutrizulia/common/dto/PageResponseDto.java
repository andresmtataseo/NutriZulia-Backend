package com.nutrizulia.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "Respuesta Paginada", description = "Estructura estándar para respuestas paginadas")
public class PageResponseDto<T> {

    @Schema(description = "Lista de elementos de la página actual")
    private List<T> content;

    @Schema(description = "Número de página actual (0-based)", example = "0")
    private int page;

    @Schema(description = "Tamaño de la página", example = "10")
    private int size;

    @Schema(description = "Total de elementos en todas las páginas", example = "25")
    private long totalElements;

    @Schema(description = "Total de páginas", example = "3")
    private int totalPages;

    @Schema(description = "Indica si es la primera página", example = "true")
    private boolean first;

    @Schema(description = "Indica si es la última página", example = "false")
    private boolean last;

    @Schema(description = "Indica si la página está vacía", example = "false")
    private boolean empty;
}