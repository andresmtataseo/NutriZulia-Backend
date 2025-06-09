package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "enfermedades")
public class Enfermedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo_internacional", nullable = false, unique = true, length = 4)
    private String codigoInternacional;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "genero", nullable = false)
    private char genero;

    @Column(name = "edad_minima", nullable = false, length = 4)
    private String edadMinima;

    @Column(name = "edad_maxima", nullable = false, length = 4)
    private String edadMaxima;

}
