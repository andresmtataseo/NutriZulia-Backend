package com.nutrizulia.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "detalles_antropometricos")
public class DetalleAntropometrico {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "altura")
    private Double altura;

    @Column(name = "talla")
    private Double talla;

    @Column(name = "circuferencia_braquial")
    private Double circuferenciaBraquial;

    @Column(name = "circuferencia_cadera")
    private Double circuferenciaCadera;

    @Column(name = "circuferencia_cintura")
    private Double circuferenciaCintura;

    @Column(name = "perimetro_cefalico")
    private Double perimetroCefalico;

    @Column(name = "pliegue_tricipital")
    private Double pliegueTricipital;

    @Column(name = "pliegue_subescapular")
    private Double pliegueSubescapular;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
