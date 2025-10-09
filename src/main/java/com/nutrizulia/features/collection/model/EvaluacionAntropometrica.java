package com.nutrizulia.features.collection.model;

import com.nutrizulia.common.enums.TipoValorCalculado;
import com.nutrizulia.features.catalog.model.TipoIndicador;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "evaluaciones_antropometricas", indexes = {
    @Index(name = "idx_eval_ant_consulta_indicador_tipo_deleted", columnList = "consulta_id, tipo_indicador_id, tipo_valor_calculado, is_deleted"),
    @Index(name = "idx_eval_ant_consulta_ind_tipo_tipo_deleted_id", columnList = "consulta_id, tipo_indicador_id, tipo_valor_calculado, is_deleted, id")
})
public class EvaluacionAntropometrica {

    @Id
    @Column(name = "id", length = 36)
    private String id;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
