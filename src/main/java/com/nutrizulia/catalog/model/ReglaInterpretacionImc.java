package com.nutrizulia.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

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

    @Column(name = "imc_minimo", precision = 12, scale = 6)
    private BigDecimal imcMinimo;

    @Column(name = "imc_maximo", precision = 12, scale = 6)
    private BigDecimal imcMaximo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

}
