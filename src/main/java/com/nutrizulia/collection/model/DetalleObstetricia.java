package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "detalles_obstetricia")
public class DetalleObstetricia extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

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

}
