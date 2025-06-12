package com.nutrizulia.model;

import com.nutrizulia.model.pre.Enfermedad;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "riesgos_biologicos")
public class RiesgoBiologico {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermedad_id", nullable = false)
    private Enfermedad  enfermedad;

    @Column(name = "is_principal", nullable = false)
    private Boolean isPrincipal;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
