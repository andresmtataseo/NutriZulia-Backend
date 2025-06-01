package com.nutrizulia.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

    @ManyToOne
    @JoinColumn(name = "municipio_sanitario_id", nullable = false)
    private MunicipioSanitario municipioSanitario;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
