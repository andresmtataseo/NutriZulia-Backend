package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.common.enums.Genero;
import com.nutrizulia.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.catalog.model.Etnia;
import com.nutrizulia.catalog.model.Nacionalidad;
import com.nutrizulia.catalog.model.Parroquia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "representantes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_representante_cedula_institucion", columnNames = {"cedula", "usuario_institucion_id"})
})
public class Representante extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id;

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

}
