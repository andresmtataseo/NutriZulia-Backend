package com.nutrizulia.features.collection.model;

import com.nutrizulia.features.catalog.model.Enfermedad;
import com.nutrizulia.features.catalog.model.RiesgoBiologico;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diagnosticos")
public class Diagnostico {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riesgo_biologico_id", nullable = false)
    private RiesgoBiologico riesgoBiologico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermedad_id")
    private Enfermedad  enfermedad;

    @Column(name = "is_principal", nullable = false)
    private Boolean isPrincipal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;


}
