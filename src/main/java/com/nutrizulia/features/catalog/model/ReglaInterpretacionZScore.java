package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "reglas_interpretaciones_z_score")
public class ReglaInterpretacionZScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "z_score_minimo", precision = 12, scale = 6)
    private BigDecimal zScoreMinimo;

    @Column(name = "z_score_maximo", precision = 12, scale = 6)
    private BigDecimal zScoreMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
}
