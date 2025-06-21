package com.nutrizulia.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "parametros_crecimientos_pedriaticos_edad")
public class ParametroCrecimientoPedriaticoEdad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @ManyToOne
    @JoinColumn(name = "grupo_etario_id", nullable = false)
    private GrupoEtario grupoEtario;

    @Column(name = "genero", nullable = false)
    private char genero;

    @Column(name = "edad_dia", nullable = false)
    private Integer edadDia;

    @Column(name = "lambda", nullable = false, precision = 12, scale = 6)
    private BigDecimal lambda;

    @Column(name = "mu", nullable = false, precision = 12, scale = 6)
    private BigDecimal mu;

    @Column(name = "sigma", nullable = false, precision = 12, scale = 6)
    private BigDecimal sigma;


}
