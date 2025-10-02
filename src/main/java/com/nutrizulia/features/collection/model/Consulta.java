package com.nutrizulia.features.collection.model;

import com.nutrizulia.common.enums.Estado;
import com.nutrizulia.common.enums.TipoConsulta;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.catalog.model.Especialidad;
import com.nutrizulia.features.catalog.model.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_institucion_id", nullable = false)
    private UsuarioInstitucion usuarioInstitucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_actividad_id", nullable = false)
    private TipoActividad tipoActividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_remitente_id", nullable = false)
    private Especialidad especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", nullable = false)
    private TipoConsulta tipoConsulta;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column(name = "fecha_hora_programada")
    private LocalDateTime fechaHoraProgramada;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "planes")
    private String planes;

    @Column(name = "fecha_hora_real", nullable = false)
    private LocalDateTime fechaHoraReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
