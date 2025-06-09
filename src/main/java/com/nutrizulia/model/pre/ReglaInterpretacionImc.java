package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reglas_interpretaciones_imc")
public class ReglaInterpretacionImc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "imc_minimo")
    private Double imcMinimo;

    @Column(name = "imc_maximo")
    private Double imcMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

}
