package com.nutrizulia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "signos_vitales")
public class SignoVital {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Consulta consulta;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "altura", nullable = false)
    private Double altura;


    private Integer glicemiaBasal;

    private Integer glicemiaPostprandial;

    private Integer glicemiaAleatoria;

    private Double hemoglobinaGlicosilada;

}
