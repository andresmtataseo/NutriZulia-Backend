package com.nutrizulia.features.collection.model;

import com.nutrizulia.common.enums.Estado;
import com.nutrizulia.common.enums.TipoConsulta;
import com.nutrizulia.features.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.features.catalog.model.Especialidad;
import com.nutrizulia.features.catalog.model.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    /**
     * Timestamp de creación del registro.
     * Se establece automáticamente al crear la entidad y no se puede modificar.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de última actualización del registro.
     * Se actualiza automáticamente cada vez que se modifica la entidad.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Indica si el registro ha sido eliminado lógicamente.
     * Por defecto es false, lo que significa que el registro está activo.
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Método ejecutado antes de persistir la entidad.
     * Asegura que los timestamps se establezcan correctamente.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        isDeleted = false;
    }

    /**
     * Método ejecutado antes de actualizar la entidad.
     * Asegura que el timestamp de actualización se establezca correctamente.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
