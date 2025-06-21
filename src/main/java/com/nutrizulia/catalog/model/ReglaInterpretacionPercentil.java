package com.nutrizulia.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "reglas_interpretaciones_percentiles")
public class ReglaInterpretacionPercentil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "percentil_minimo", precision = 12, scale = 6)
    private BigDecimal percentilMinimo;

    @Column(name = "percentil_maximo", precision = 12, scale = 6)
    private BigDecimal percentilMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

}
