package com.nutrizulia.model.movil;

import com.nutrizulia.enums.TipoValorCalculado;
import com.nutrizulia.model.pre.TipoIndicador;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "evaluaciones_antropometricas")
public class EvaluacionAntropometrica {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_antropometrico_id", nullable = false)
    private DetalleAntropometrico detalleAntropometrico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_indicador_id", nullable = false)
    private TipoIndicador tipoIndicador;

    @Column(name = "valor_calculado", nullable = false, precision = 12, scale = 6)
    private BigDecimal valorCalculado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_valor_calculado", nullable = false)
    private TipoValorCalculado tipoValorCalculado;

    @Column(name = "diagnostico_antropometrico", nullable = false)
    private String diagnosticoAntropometrico;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
