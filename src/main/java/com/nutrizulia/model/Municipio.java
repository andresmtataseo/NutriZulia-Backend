package com.nutrizulia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "municipios")
@Schema(description = "Entidad que representa un municipio dentro de un estado")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Identificador único del municipio", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    @NotNull(message = "El estado no puede ser nulo")
    @Schema(description = "Estado al que pertenece el municipio", required = true)
    private Estado estado;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre del municipio no puede estar vacío")
    @Size(max = 255, message = "El nombre es de máximo 255 caracteres")
    @Schema(description = "Nombre del municipio", example = "Maracaibo", required = true)
    private String nombre;
}
