package com.nutrizulia.collection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad DetalleAntropometrico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleAntropometricoDto {
    
    private UUID id;
    private UUID consultaId;
    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal talla;
    private BigDecimal circuferenciaBraquial;
    private BigDecimal circuferenciaCadera;
    private BigDecimal circuferenciaCintura;
    private BigDecimal perimetroCefalico;
    private BigDecimal pliegueTricipital;
    private BigDecimal pliegueSubescapular;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}