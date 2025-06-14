package com.nutrizulia.model;

import com.nutrizulia.enums.Genero;
import com.nutrizulia.model.pre.Etnia;
import com.nutrizulia.model.pre.Nacionalidad;
import com.nutrizulia.model.pre.Parroquia;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "representantes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_representante_cedula_institucion", columnNames = {"cedula", "usuario_institucion_id"})
})
public class Representante {

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

    @Column(name = "genero", nullable = false)
    private char genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etnia_id", nullable = false)
    private Etnia etnia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nacionalidad_id", nullable = false)
    private Nacionalidad nacionalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parroquia_id", nullable = false)
    private Parroquia parroquia;

    @Column(name = "domicilio", nullable = false)
    private String domicilio;

    @Column(name = "telefono", length = 12)
    private String telefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
