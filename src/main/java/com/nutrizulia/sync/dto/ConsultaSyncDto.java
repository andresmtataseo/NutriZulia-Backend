package com.nutrizulia.sync.dto;

import com.nutrizulia.common.enums.Estado;
import com.nutrizulia.common.enums.TipoConsulta;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para sincronización de entidad Consulta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSyncDto {
    
    private UUID id;
    private Integer usuarioInstitucionId;
    private UUID pacienteId;
    private Integer tipoActividadId;
    private Integer especialidadRemienteId;
    private TipoConsulta tipoConsulta;
    private String motivoConsulta;
    private LocalDate fechaHoraProgramada;
    private String observaciones;
    private String planes;
    private LocalDate fechaHoraReal;
    private Estado estado;
    
    // Campos de auditoría para sincronización
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}