package com.nutrizulia.features.collection.model;

import com.nutrizulia.common.enums.Genero;
import com.nutrizulia.features.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.features.catalog.model.Etnia;
import com.nutrizulia.features.catalog.model.Nacionalidad;
import com.nutrizulia.features.catalog.model.Parroquia;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "representantes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_representante_cedula_institucion", columnNames = {"cedula", "usuario_institucion_id"})
})
public class Representante {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_institucion_id", nullable = false)
    private UsuarioInstitucion usuarioInstitucion;

    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etnia_id", nullable = false)
    private Etnia etnia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nacionalidad_id", nullable = false)
    private Nacionalidad nacionalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parroquia_id", nullable = false)
    private Parroquia parroquia;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "telefono", length = 12)
    private String telefono;

    @Column(name = "correo")
    private String correo;

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
