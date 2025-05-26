package com.nutrizulia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "etnias")
@Schema(description = "Entidad que representa una etnia")
public class Etnia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único de la etnia", example = "1")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre de la etnia", example = "Afrovenezolana")
    private String nombre;

//    @OneToMany(mappedBy = "etnia", fetch = FetchType.LAZY)
//    @JsonIgnore
//    @Schema(hidden = true) // Oculta este campo en Swagger
//    private List<Paciente> pacientes;
}
