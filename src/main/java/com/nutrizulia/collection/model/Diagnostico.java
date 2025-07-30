package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.catalog.model.Enfermedad;
import com.nutrizulia.catalog.model.RiesgoBiologico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "diagnosticos")
public class Diagnostico extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

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

}
