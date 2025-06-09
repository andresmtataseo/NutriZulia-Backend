package com.nutrizulia.model;

import com.nutrizulia.model.pre.TipoActividad;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_institucion_id", nullable = false)
    private UsuarioInstitucion usuarioInstitucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_actividad", nullable = false)
    private TipoActividad tipoActividad;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "descripcion_general")
    private String descripcionGeneral;

    @Column(name = "cantidad_participantes")
    private Integer cantidadParticipantes;

    @Column(name = "cantidad_sesiones_realizadas")
    private Integer cantidadSesionesRealizadas;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "tema_principal")
    private String temaPrincipal;

    @Column(name = "programas_implementados")
    private String programasImplementados;

    @Column(name = "url_evidencia")
    private String urlEvidencia;
}
