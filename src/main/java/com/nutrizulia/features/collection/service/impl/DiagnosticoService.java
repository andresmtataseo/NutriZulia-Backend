package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DiagnosticoDto;
import com.nutrizulia.features.collection.mapper.DiagnosticoMapper;
import com.nutrizulia.features.collection.model.Diagnostico;
import com.nutrizulia.features.collection.repository.DiagnosticoRepository;
import com.nutrizulia.features.collection.service.IDiagnosticoService;
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
public class DiagnosticoService implements IDiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final DiagnosticoMapper diagnosticoMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDiagnosticos(List<DiagnosticoDto> diagnosticos) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DiagnosticoDto dto : diagnosticos) {
            try {
                procesarDiagnostico(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDiagnostico(DiagnosticoDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<Diagnostico> existenteOpt = diagnosticoRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDiagnostico(dto, response);
            } else {
                actualizarDiagnosticoExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para diagnóstico {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando diagnóstico {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDiagnostico(DiagnosticoDto dto, BatchSyncResponseDTO response) {
        try {
            Diagnostico nuevo = diagnosticoMapper.toEntity(dto);
            diagnosticoRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Diagnóstico creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando diagnóstico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDiagnosticoExistente(DiagnosticoDto dto, Diagnostico existente, 
                                               BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                Diagnostico actualizado = diagnosticoMapper.toEntity(dto);
                diagnosticoRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Diagnóstico actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Diagnóstico ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Diagnóstico sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando diagnóstico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String diagnosticoId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, diagnosticoId, errorMessage);
        log.error("Error sincronizando diagnóstico {}: {}", diagnosticoId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String diagnosticoId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(diagnosticoId)
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
            if (message.contains("foreign key")) {
                return "Referencia a datos no existentes";
            } else if (message.contains("unique")) {
                return "Datos duplicados";
            }
        }
        return "Violación de restricción de base de datos";
    }
}
