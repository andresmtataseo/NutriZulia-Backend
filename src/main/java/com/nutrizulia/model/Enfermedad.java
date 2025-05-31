package com.nutrizulia.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enfermedades")
public class Enfermedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo_internacional", nullable = false, unique = true)
    private String codigoInternacional;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "limite_genero", nullable = false)
    private String limiteGenero;

    @Column(name = "limite_inferior_edad", nullable = false)
    private String limiteInferiorEdad;

    @Column(name = "limite_superior_edad", nullable = false)
    private String limiteSuperiorEdad;

}
