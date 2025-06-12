package com.nutrizulia.model;

import com.nutrizulia.enums.TipoLactancia;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "detalles_obstetricia")
public class DetalleObstetricia {

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

    @Column(name = "peso_pre_embarazo")
    private Double pesoPreEmbarazo ;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
