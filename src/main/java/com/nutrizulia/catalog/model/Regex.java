package com.nutrizulia.catalog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "regex")
public class Regex {

    @Id
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "expression", nullable = false)
    private String expression;

}
