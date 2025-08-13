package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteRepresentanteDto;
import com.nutrizulia.features.collection.mapper.PacienteRepresentanteMapper;
import com.nutrizulia.features.collection.model.PacienteRepresentante;
import com.nutrizulia.features.collection.repository.PacienteRepresentanteRepository;
import com.nutrizulia.features.collection.service.IPacienteRepresentanteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PacienteRepresentanteService implements IPacienteRepresentanteService {

    private final PacienteRepresentanteRepository pacienteRepresentanteRepository;
    private final PacienteRepresentanteMapper pacienteRepresentanteMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncPacientesRepresentantes(List<PacienteRepresentanteDto> pacienteRepresentantes) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (PacienteRepresentanteDto dto : pacienteRepresentantes) {
            try {
                procesarPacienteRepresentante(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarPacienteRepresentante(PacienteRepresentanteDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<PacienteRepresentante> existenteOpt = pacienteRepresentanteRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoPacienteRepresentante(dto, response);
            } else {
                actualizarPacienteRepresentanteExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para paciente-representante {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando paciente-representante {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoPacienteRepresentante(PacienteRepresentanteDto dto, BatchSyncResponseDTO response) {
        try {
            PacienteRepresentante nuevo = pacienteRepresentanteMapper.toEntity(dto);
            pacienteRepresentanteRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Paciente-representante creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando paciente-representante {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarPacienteRepresentanteExistente(PacienteRepresentanteDto dto, PacienteRepresentante existente, 
                                                         BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                PacienteRepresentante actualizado = pacienteRepresentanteMapper.toEntity(dto);
                pacienteRepresentanteRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Paciente-representante actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Paciente-representante ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Paciente-representante sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando paciente-representante {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String pacienteRepresentanteId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, pacienteRepresentanteId, errorMessage);
        log.error("Error sincronizando paciente-representante {}: {}", pacienteRepresentanteId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String pacienteRepresentanteId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(pacienteRepresentanteId)
                .reason(reason)
                .build();
        response.getFailed().add(failedRecord);
    }
    
    private String determinarMensajeError(Exception e) {
        if (e instanceof DataIntegrityViolationException) {
            return "Error de integridad de datos: " + extractConstraintError((DataIntegrityViolationException) e);
        } else if (e instanceof IllegalArgumentException) {
            return "Datos inválidos: " + e.getMessage();
        } else {
            return "Error interno del servidor";
        }
    }
    
    private String extractConstraintError(DataIntegrityViolationException e) {
        String message = e.getMessage();
        if (message != null) {
            if (message.contains("paciente_id")) {
                return "Paciente no válido";
            } else if (message.contains("representante_id")) {
                return "Representante no válido";
            } else if (message.contains("foreign key")) {
                return "Referencia a datos no existentes";
            } else if (message.contains("unique")) {
                return "Relación duplicada";
            }
        }
        return "Violación de restricción de base de datos";
    }
}
