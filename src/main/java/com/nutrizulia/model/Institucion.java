package com.nutrizulia.model;

import com.nutrizulia.model.pre.MunicipioSanitario;
import com.nutrizulia.model.pre.TipoInstitucion;
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
    @JoinColumn(name = "municipio_sanitario_id", nullable = false)
    private MunicipioSanitario municipioSanitario;

    @ManyToOne
    @JoinColumn(name = "tipo_institucion_id", nullable = false)
    private TipoInstitucion tipoInstitucion;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
