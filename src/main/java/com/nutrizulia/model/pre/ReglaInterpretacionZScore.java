package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "z_score_minimo")
    private Double zScoreMinimo;

    @Column(name = "z_score_maximo")
    private Double zScoreMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
}
