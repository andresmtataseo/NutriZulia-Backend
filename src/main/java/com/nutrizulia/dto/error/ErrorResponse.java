package com.nutrizulia.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Error Response", description = "Representa una respuesta de error estandarizada de la API, incluyendo errores de validación si aplica.")
public class ErrorResponse {

    @Schema(description = "Marca de tiempo UTC en que ocurrió el error")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP del error")
    private int status;

    @Schema(description = "Nombre genérico del error HTTP")
    private String error;

    @Schema(description = "Mensaje descriptivo del error, legible para el usuario final o desarrollador")
    private String message;

    @Schema(description = "Ruta de la solicitud que provocó el error")
    private String path;

    @Schema(
            description = "Lista de errores de validación específicos para campos, presente solo en errores 400 (Bad Request).",
            nullable = true
    )
    private List<Map<String, String>> validationErrors;
}