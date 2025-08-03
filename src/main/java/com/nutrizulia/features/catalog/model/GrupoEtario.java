package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "grupos_etarios")
public class GrupoEtario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "edad_mes_minima", nullable = false)
    private Integer edadMesMinima;

    @Column(name = "edad_mes_maxima", nullable = false)
    private Integer edadMesMaxima;

}
