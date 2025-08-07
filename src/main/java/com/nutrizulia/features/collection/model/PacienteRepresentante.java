package com.nutrizulia.features.collection.model;

import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.catalog.model.Parentesco;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pacientes_representantes")
public class PacienteRepresentante {

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
    @JoinColumn(name = "representante_id", nullable = false)
    private Representante representante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentesco_id", nullable = false)
    private Parentesco parentesco;

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
