package com.nutrizulia.model.movil;

import com.nutrizulia.model.admin.UsuarioInstitucion;
import com.nutrizulia.model.pre.Parentesco;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "pacientes_representantes")
public class PacienteRepresentante {

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

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
