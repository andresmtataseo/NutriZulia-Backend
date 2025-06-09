package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "percentil_minimo")
    private Double percentilMinimo;

    @Column(name = "percentil_maximo")
    private Double percentilMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

}
