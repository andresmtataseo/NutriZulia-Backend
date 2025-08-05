package com.nutrizulia.features.collection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa los valores metabólicos y de laboratorio registrados durante una consulta")
public class DetalleMetabolicoDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenecen estos valores", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String consulta_id;
    
    @Schema(description = "Glicemia basal en mg/dL", example = "95")
    private Integer glicemia_basal;
    
    @Schema(description = "Glicemia postprandial en mg/dL", example = "140")
    private Integer glicemia_postprandial;
    
    @Schema(description = "Glicemia aleatoria en mg/dL", example = "110")
    private Integer glicemia_aleatoria;
    
    @Schema(description = "Hemoglobina glicosilada en porcentaje", example = "5.8")
    private BigDecimal hemoglobina_glicosilada;
    
    @Schema(description = "Triglicéridos en mg/dL", example = "150")
    private Integer trigliceridos;
    
    @Schema(description = "Colesterol total en mg/dL", example = "200")
    private Integer colesterol_total;
    
    @Schema(description = "Colesterol HDL (bueno) en mg/dL", example = "50")
    private Integer colesterol_hdl;
    
    @Schema(description = "Colesterol LDL (malo) en mg/dL", example = "120")
    private Integer colesterol_ldl;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}