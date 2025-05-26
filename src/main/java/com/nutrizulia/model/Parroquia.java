package com.nutrizulia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parroquias")
@Schema(description = "Entidad que representa una parroquia dentro de un municipio")
public class Parroquia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único de la parroquia", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "municipio_id", nullable = false)
    @Schema(description = "Municipio al que pertenece la parroquia", required = true)
    private Municipio municipio;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre debe tener máximo 255 caracteres")
    @Schema(description = "Nombre de la parroquia", example = "San Francisco", required = true)
    private String nombre;

}