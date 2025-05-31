package com.nutrizulia.model;

import com.nutrizulia.enums.Estado;
import com.nutrizulia.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultas")
public class Consulta {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_principal_id")
    private Enfermedad enfermedadPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_secundario_id")
    private Enfermedad enfermedadSecundario;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_hora_real", nullable = false)
    private LocalDateTime fechaHoraReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private SignoVital signosVitales;

}
