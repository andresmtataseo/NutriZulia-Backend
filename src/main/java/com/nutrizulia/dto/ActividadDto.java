package com.nutrizulia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Entidad que representa una actividad de salud realizada por un usuario en una institución")
public class ActividadDto {

    @Schema(description = "Identificador único de la actividad", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Identificador de la relación usuario-institución-rol a la que pertenece esta actividad. Es el enlace al contexto de seguridad del usuario dentro de una institución.", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;

    @Schema(description = "Identificador del tipo de actividad que se realizó (ej. consulta, charla, taller).", example = "2")
    @NotNull(message = "Identificador del tipo de actividad es obligatorio")
    private Integer tipo_actividad_id;

    @Schema(description = "Fecha en que se llevó a cabo la actividad.", example = "2024-05-20")
    @NotNull(message = "La fecha de realizacion es obligatoria")
    private LocalDate fecha;

    @Size(max = 255, message = "La dirección es de máximo 255 caracteres")
    @Schema(description = "La dirección o lugar específico donde se realizó la actividad (ej. nombre de la escuela, centro comunitario).", example = "UNIDAD EDUCATIVA LOS PINOS")
    private String direccion;

    @Schema(description = "Descripción general o un resumen conciso de la actividad realizada.", example = "Jornada de despistaje nutricional y charlas informativas sobre hábitos alimenticios saludables para la comunidad.")
    private String descripcion_general;

    @Schema(description = "Número total de personas que participaron o se vieron beneficiadas por la actividad.", example = "50")
    private Integer cantidad_participantes;

    @Schema(description = "Número de sesiones o encuentros que se realizaron como parte de esta actividad.", example = "3")
    private Integer cantidad_sesiones_realizadas;

    @Schema(description = "Duración total de la actividad en minutos.", example = "120")
    private Integer duracion_minutos;

    @Schema(description = "Tema principal o el enfoque central de la actividad.", example = "Importancia de una dieta equilibrada en niños escolares.")
    private String tema_principal;

    @Schema(description = "Detalles de los programas o iniciativas específicas implementadas durante la actividad.", example = "Programa de control de peso infantil, iniciativa 'Comer sano, vivir feliz'.")
    private String programas_implementados;

    @Schema(description = "URL o enlace a la evidencia de la actividad (ej. fotos, videos, documentos).", example = "https://ejemplo.com/evidencia/actividad_123.jpg")
    private String url_evidencia;
}