package com.nutrizulia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instituciones")
@Schema(description = "Entidad que representa una institución")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "ID único de la institución", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_institucion_id", nullable = false)
    @NotNull(message = "El tipo de institución es obligatorio")
    @Schema(description = "Tipo de institución asociada", required = true)
    private TipoInstitucion tipoInstitucion;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre de la institución es obligatorio")
    @Schema(description = "Nombre de la institución", example = "Hospital General del Sur", required = true)
    private String nombre;
}
