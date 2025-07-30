package com.nutrizulia.collection.model;

import com.nutrizulia.common.entity.BaseEntity;
import com.nutrizulia.userinstitution.model.UsuarioInstitucion;
import com.nutrizulia.catalog.model.Parentesco;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pacientes_representantes")
public class PacienteRepresentante extends BaseEntity {

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
    @JoinColumn(name = "representante_id", nullable = false)
    private Representante representante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentesco_id", nullable = false)
    private Parentesco parentesco;

}
