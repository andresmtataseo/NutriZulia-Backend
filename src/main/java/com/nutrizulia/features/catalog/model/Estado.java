package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estados")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
