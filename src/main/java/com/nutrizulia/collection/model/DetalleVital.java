package com.nutrizulia.collection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "detalles_vitales")
public class DetalleVital {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "tension_arterial_sistolica")
    private Integer tensionArterialSistolica;

    @Column(name = "tension_arterial_diastolica")
    private Integer tensionArterialDiastolica;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "temperatura", precision = 12, scale = 6)
    private BigDecimal temperatura;

    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;

    @Column(name = "pulso")
    private Integer pulso;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
