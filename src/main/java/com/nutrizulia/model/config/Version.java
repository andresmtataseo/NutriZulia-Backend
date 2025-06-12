package com.nutrizulia.model.config;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_tabla", nullable = false)
    private String nombreTabla;

    @Column(name = "version", nullable = false)
    private Integer version;

}
