package com.nutrizulia.features.collection.model;

import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.catalog.model.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @Column(name = "id", length = 36)
    private String id;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
