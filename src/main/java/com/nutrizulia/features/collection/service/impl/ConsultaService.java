package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.ConsultaDto;
import com.nutrizulia.features.collection.mapper.ConsultaMapper;
import com.nutrizulia.features.collection.model.Consulta;
import com.nutrizulia.features.collection.repository.ConsultaRepository;
import com.nutrizulia.features.collection.service.IConsultaService;
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
public class ConsultaService implements IConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncConsultas(List<ConsultaDto> consultas) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (ConsultaDto dto : consultas) {
            try {
                procesarConsulta(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarConsulta(ConsultaDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<Consulta> existenteOpt = consultaRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevaConsulta(dto, response);
            } else {
                actualizarConsultaExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para consulta {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando consulta {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevaConsulta(ConsultaDto dto, BatchSyncResponseDTO response) {
        try {
            Consulta nueva = consultaMapper.toEntity(dto);
            consultaRepository.save(nueva);
            response.getSuccess().add(dto.getId());
            log.debug("Consulta creada exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando consulta {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarConsultaExistente(ConsultaDto dto, Consulta existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                Consulta actualizada = consultaMapper.toEntity(dto);
                consultaRepository.save(actualizada);
                response.getSuccess().add(dto.getId());
                log.debug("Consulta actualizada exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Consulta ya actualizada en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Consulta sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando consulta {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String consultaId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, consultaId, errorMessage);
        log.error("Error sincronizando consulta {}: {}", consultaId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String consultaId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(consultaId)
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
            if (message.contains("usuario_institucion_id")) {
                return "Usuario-institución no válido";
            } else if (message.contains("paciente_id")) {
                return "Paciente no válido";
            } else if (message.contains("foreign key")) {
                return "Referencia a datos no existentes";
            } else if (message.contains("unique")) {
                return "Datos duplicados";
            }
        }
        return "Violación de restricción de base de datos";
    }
}
