package com.nutrizulia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "evaluaciones_antropometricas")
public class EvaluacionAntropometrica {

    @Id
    @Column(name = "id")
    private UUID id;

}
