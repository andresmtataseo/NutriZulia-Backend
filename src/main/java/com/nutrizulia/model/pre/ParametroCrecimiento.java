package com.nutrizulia.model.pre;

import com.nutrizulia.enums.UnidadEdad;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parametros_crecimiento")
public class ParametroCrecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "genero", nullable = false)
    private char genero;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "unidad_edad", nullable = false)
    private UnidadEdad unidadEdad;

    @Column(name = "lambda", nullable = false)
    private Double lambda;

    @Column(name = "mu", nullable = false)
    private Double mu;

    @Column(name = "sigma", nullable = false)
    private Double sigma;


}
