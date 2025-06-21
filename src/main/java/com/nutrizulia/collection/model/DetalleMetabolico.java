package com.nutrizulia.collection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "detalles_metabolicos")
public class DetalleMetabolico {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "glicemia_basal")
    private Integer glicemiaBasal;

    @Column(name = "glicemia_postprandial")
    private Integer glicemaPostprandial ;

    @Column(name = "glicemia_aleatoria")
    private Integer glicemiaAleatoria;

    @Column(name = "hemoglobina_glicosilada", precision = 12, scale = 6)
    private BigDecimal hemoglobinaGlicosilada;

    @Column(name = "trigliceridos")
    private Integer trigliceridos;

    @Column(name = "colesterol_total")
    private Integer colesterolTotal;

    @Column(name = "colesterol_hdl")
    private Integer colesterolHdl;

    @Column(name = "colesterol_ldl")
    private Integer colesterolLdl;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
