package com.nutrizulia.model.pre;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "etnias")
public class Etnia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

}
