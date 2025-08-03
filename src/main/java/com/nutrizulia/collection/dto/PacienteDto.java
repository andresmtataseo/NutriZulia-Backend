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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un paciente registrado en el sistema de salud")
public class PacienteDto {
    
    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "El identificador no puede ser nulo")
    @Size(max = 36, message = "El identificador no puede tener más de 36 caracteres")
    private String id;
    
    @Schema(description = "Identificador de la relación usuario-institución-rol que registra al paciente", example = "14")
    @NotNull(message = "El identificador de la relación usuario-institución-rol no puede ser nulo")
    private Integer usuario_institucion_id;
    
    @Schema(description = "Número de cédula de identidad del paciente", example = "V-12345678")
    @NotNull(message = "La cédula es obligatoria")
    @Size(max = 13, message = "La cédula no puede tener más de 13 caracteres")
    private String cedula;
    
    @Schema(description = "Nombres del paciente", example = "Juan Carlos")
    @NotNull(message = "Los nombres son obligatorios")
    private String nombres;
    
    @Schema(description = "Apellidos del paciente", example = "Pérez González")
    @NotNull(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-05-15")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fecha_nacimiento;
    
    @Schema(description = "Género del paciente", example = "MASCULINO")
    @NotNull(message = "El género es obligatorio")
    private Genero genero;
    
    @Schema(description = "Identificador de la etnia del paciente", example = "1")
    @NotNull(message = "La etnia es obligatoria")
    private Integer etnia_id;
    
    @Schema(description = "Identificador de la nacionalidad del paciente", example = "1")
    @NotNull(message = "La nacionalidad es obligatoria")
    private Integer nacionalidad_id;
    
    @Schema(description = "Identificador de la parroquia donde reside el paciente", example = "1")
    @NotNull(message = "La parroquia es obligatoria")
    private Integer parroquia_id;
    
    @Schema(description = "Dirección de domicilio del paciente", example = "Av. Principal, Casa #123, Sector Los Pinos")
    private String domicilio;
    
    @Schema(description = "Número de teléfono del paciente", example = "0416-1234567")
    @Size(max = 12, message = "El teléfono no puede tener más de 12 caracteres")
    private String telefono;
    
    @Schema(description = "Correo electrónico del paciente", example = "juan.perez@email.com")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;
    
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created_at;
    
    @Schema(description = "Fecha de última actualización del registro")
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime updated_at;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente")
    @NotNull(message = "Debe especificarse si el registro ha sido eliminado")
    private Boolean is_deleted;
}