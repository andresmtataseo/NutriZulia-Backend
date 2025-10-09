package com.nutrizulia.features.user.model;

import com.nutrizulia.features.catalog.model.Rol;
import com.nutrizulia.features.catalog.model.Institucion;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "usuarios_instituciones", indexes = {
    @Index(name = "idx_usuarios_instituciones_institucion_enabled", columnList = "institucion_id, is_enabled"),
    @Index(name = "idx_usuarios_instituciones_usuario", columnList = "usuario_id"),
    @Index(name = "idx_usuarios_instituciones_rol", columnList = "rol_id")
})
public class UsuarioInstitucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

}
