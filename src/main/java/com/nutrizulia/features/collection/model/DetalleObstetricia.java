package com.nutrizulia.features.collection.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "detalles_obstetricias")
public class DetalleObstetricia {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "esta_embarazada")
    private Boolean estaEmbarazada;

    @Column(name = "fecha_ultima_menstruacion")
    private LocalDate fechaUltimaMenstruacion;

    @Column(name = "semanas_gestacion")
    private Integer semanasGestacion;

    @Column(name = "peso_pre_embarazo", precision = 12, scale = 6)
    private BigDecimal pesoPreEmbarazo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted ;

}
