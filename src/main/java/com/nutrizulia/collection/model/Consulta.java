package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.common.enums.Estado;
import com.nutrizulia.common.enums.TipoConsulta;
import com.nutrizulia.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.catalog.model.Especialidad;
import com.nutrizulia.catalog.model.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "consultas")
public class Consulta extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_institucion_id", nullable = false)
    private UsuarioInstitucion usuarioInstitucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_actividad_id")
    private TipoActividad tipoActividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_remiente_id")
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

}
