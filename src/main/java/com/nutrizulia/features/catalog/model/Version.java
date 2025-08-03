package com.nutrizulia.features.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "version")
public class Version {

    @Id
    @Column(name = "nombre_tabla")
    private String nombreTabla;

    @Column(name = "version", nullable = false)
    private Integer version;

}
