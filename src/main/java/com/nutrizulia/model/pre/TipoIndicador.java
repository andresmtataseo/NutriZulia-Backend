package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipos_indicadores")
public class TipoIndicador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "grupo_etario_id", nullable = false)
    private GrupoEtario grupoEtario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

}