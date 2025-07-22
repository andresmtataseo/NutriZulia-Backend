package com.nutrizulia.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Esquema genérico para las respuestas de la API, incluyendo mensajes de éxito o error.")
public class ApiResponseDto {

    @Schema(description = "Código de estado HTTP de la respuesta", example = "200")
    private int status;

    @Schema(description = "Mensaje descriptivo de la respuesta", example = "Operación exitosa")
    private String message;

    @Schema(description = "Marca de tiempo de cuando ocurrió la respuesta", example = "2025-06-19T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Ruta de la petición que generó la respuesta", example = "/api/auth/sign-in")
    private String path;

    @Schema(description = "Detalles específicos de errores, como errores de validación de campos")
    private List<String> errors;

}