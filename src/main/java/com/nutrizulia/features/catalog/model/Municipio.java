package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "municipios")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
