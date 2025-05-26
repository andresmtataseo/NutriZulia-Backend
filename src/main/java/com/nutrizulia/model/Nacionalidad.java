package com.nutrizulia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nacionalidad")
@Schema(description = "Entidad que representa la nacionalidad de un paciente.")
public class Nacionalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único de la nacionalidad", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre de la nacionalidad no puede estar vacío")
    @Schema(description = "Nombre de la nacionalidad", example = "Venezolana")
    private String nombre;

}
