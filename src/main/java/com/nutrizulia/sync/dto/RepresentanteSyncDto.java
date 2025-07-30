package com.nutrizulia.sync.dto;

import com.nutrizulia.common.enums.Genero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad Representante
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepresentanteSyncDto {
    
    private UUID id;
    private Integer usuarioInstitucionId;
    private String cedula;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private Genero genero;
    private Integer etniaId;
    private Integer nacionalidadId;
    private Integer parroquiaId;
    private String domicilio;
    private String telefono;
    private String correo;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}