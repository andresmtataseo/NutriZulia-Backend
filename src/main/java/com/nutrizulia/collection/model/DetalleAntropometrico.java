package com.nutrizulia.collection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
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

    @Column(name = "peso", precision = 12, scale = 6)
    private BigDecimal peso;

    @Column(name = "altura", precision = 12, scale = 6)
    private BigDecimal altura;

    @Column(name = "talla", precision = 12, scale = 6)
    private BigDecimal talla;

    @Column(name = "circuferencia_braquial", precision = 12, scale = 6)
    private BigDecimal circuferenciaBraquial;

    @Column(name = "circuferencia_cadera", precision = 12, scale = 6)
    private BigDecimal circuferenciaCadera;

    @Column(name = "circuferencia_cintura", precision = 12, scale = 6)
    private BigDecimal circuferenciaCintura;

    @Column(name = "perimetro_cefalico", precision = 12, scale = 6)
    private BigDecimal perimetroCefalico;

    @Column(name = "pliegue_tricipital", precision = 12, scale = 6)
    private BigDecimal pliegueTricipital;

    @Column(name = "pliegue_subescapular", precision = 12, scale = 6)
    private BigDecimal pliegueSubescapular;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
