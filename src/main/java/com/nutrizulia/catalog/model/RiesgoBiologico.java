package com.nutrizulia.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "riesgos_biologicos")
public class RiesgoBiologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "genero", nullable = false)
    private char genero;

    @Column(name = "edad_mes_minima")
    private Integer edadMesMinima;

    @Column(name = "edad_mes_maxima")
    private Integer edadMesMaxima;

}
