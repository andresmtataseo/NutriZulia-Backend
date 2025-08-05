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
@Schema(description = "Entidad que representa las medidas antropométricas registradas durante una consulta")
public class DetalleAntropometricoDto {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador único de la consulta a la que pertenecen estas medidas", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "El identificador de la consulta no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String consulta_id;
    
    @Schema(description = "Peso corporal en kilogramos", example = "65.5")
    private BigDecimal peso;
    
    @Schema(description = "Altura en centímetros", example = "165.0")
    private BigDecimal altura;
    
    @Schema(description = "Talla en centímetros", example = "165.0")
    private BigDecimal talla;
    
    @Schema(description = "Circunferencia braquial en centímetros", example = "28.5")
    private BigDecimal circuferencia_braquial;
    
    @Schema(description = "Circunferencia de cadera en centímetros", example = "95.0")
    private BigDecimal circuferencia_cadera;
    
    @Schema(description = "Circunferencia de cintura en centímetros", example = "80.0")
    private BigDecimal circuferencia_cintura;
    
    @Schema(description = "Perímetro cefálico en centímetros", example = "55.0")
    private BigDecimal perimetro_cefalico;
    
    @Schema(description = "Pliegue tricipital en milímetros", example = "15.5")
    private BigDecimal pliegue_tricipital;
    
    @Schema(description = "Pliegue subescapular en milímetros", example = "12.0")
    private BigDecimal pliegue_subescapular;

    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;

    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;

    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}