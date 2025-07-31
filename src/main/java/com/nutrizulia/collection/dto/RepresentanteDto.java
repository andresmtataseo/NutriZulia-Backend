package com.nutrizulia.collection.dto;

import com.nutrizulia.common.enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un representante legal o responsable de un paciente")
public class RepresentanteDto {
    
    @Schema(description = "Identificador único del representante", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    
    @Schema(description = "Identificador de la relación usuario-institución-rol que registra al representante", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;
    
    @Schema(description = "Número de cédula de identidad del representante", example = "1234567890")
    @NotNull(message = "La cédula es obligatoria")
    @Size(max = 10, message = "La cédula no puede tener más de 10 caracteres")
    private String cedula;
    
    @Schema(description = "Nombres del representante", example = "María Elena")
    @NotNull(message = "Los nombres son obligatorios")
    private String nombres;
    
    @Schema(description = "Apellidos del representante", example = "García López")
    @NotNull(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @Schema(description = "Fecha de nacimiento del representante", example = "1985-03-20")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fecha_nacimiento;
    
    @Schema(description = "Género del representante", example = "FEMENINO")
    @NotNull(message = "El género es obligatorio")
    private Genero genero;
    
    @Schema(description = "Identificador de la etnia del representante", example = "1")
    @NotNull(message = "La etnia es obligatoria")
    private Integer etnia_id;
    
    @Schema(description = "Identificador de la nacionalidad del representante", example = "1")
    @NotNull(message = "La nacionalidad es obligatoria")
    private Integer nacionalidad_id;
    
    @Schema(description = "Identificador de la parroquia donde reside el representante", example = "1")
    @NotNull(message = "La parroquia es obligatoria")
    private Integer parroquia_id;
    
    @Schema(description = "Dirección de domicilio del representante", example = "Calle 5, Casa #45, Urbanización El Valle")
    private String domicilio;
    
    @Schema(description = "Número de teléfono del representante", example = "0424-1234567")
    @Size(max = 12, message = "El teléfono no puede tener más de 12 caracteres")
    private String telefono;
    
    @Schema(description = "Correo electrónico del representante", example = "maria.garcia@email.com")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;
    
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;
    
    @Schema(description = "Fecha de última actualización del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updated_at;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean is_deleted;
}