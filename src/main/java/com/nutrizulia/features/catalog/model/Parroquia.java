package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parroquias")
public class Parroquia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @Column(name = "nombre", nullable = false)
    private String nombre;

}