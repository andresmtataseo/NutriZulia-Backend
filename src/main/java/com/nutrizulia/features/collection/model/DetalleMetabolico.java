package com.nutrizulia.features.collection.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "detalles_metabolicos")
public class DetalleMetabolico {

    @Id
    @Column(name = "id", length = 36)
    private String id;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted ;

}
