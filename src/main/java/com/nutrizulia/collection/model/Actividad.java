package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.catalog.model.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "actividades")
public class Actividad extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_institucion_id", nullable = false)
    private UsuarioInstitucion usuarioInstitucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_actividad_id", nullable = false)
    private TipoActividad tipoActividad;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "descripcion_general")
    private String descripcionGeneral;

    @Column(name = "cantidad_participantes")
    private Integer cantidadParticipantes;

    @Column(name = "cantidad_sesiones")
    private Integer cantidadSesiones;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "tema_principal")
    private String temaPrincipal;

    @Column(name = "programas_implementados")
    private String programasImplementados;

    @Column(name = "url_evidencia")
    private String urlEvidencia;
}
