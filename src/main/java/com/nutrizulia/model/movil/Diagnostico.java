package com.nutrizulia.model.movil;

import com.nutrizulia.model.pre.Enfermedad;
import com.nutrizulia.model.pre.RiesgoBiologico;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "diagnosticos")
public class Diagnostico {

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

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
