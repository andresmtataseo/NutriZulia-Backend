package com.nutrizulia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instituciones")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_institucion_id", nullable = false)
    private TipoInstitucion tipoInstitucion;

    @Column(name = "nombre", nullable = false)
    private String nombre;

}
