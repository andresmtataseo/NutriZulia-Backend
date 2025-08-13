package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleVitalDto;
import com.nutrizulia.features.collection.mapper.DetalleVitalMapper;
import com.nutrizulia.features.collection.model.DetalleVital;
import com.nutrizulia.features.collection.repository.DetalleVitalRepository;
import com.nutrizulia.features.collection.service.IDetalleVitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetalleVitalService implements IDetalleVitalService {

    private final DetalleVitalRepository detalleVitalRepository;
    private final DetalleVitalMapper detalleVitalMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDetallesVital(List<DetalleVitalDto> detallesVitales) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DetalleVitalDto dto : detallesVitales) {
            try {
                procesarDetalleVital(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDetalleVital(DetalleVitalDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<DetalleVital> existenteOpt = detalleVitalRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDetalleVital(dto, response);
            } else {
                actualizarDetalleVitalExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para detalle vital {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando detalle vital {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDetalleVital(DetalleVitalDto dto, BatchSyncResponseDTO response) {
        try {
            DetalleVital nuevo = detalleVitalMapper.toEntity(dto);
            detalleVitalRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Detalle vital creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando detalle vital {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDetalleVitalExistente(DetalleVitalDto dto, DetalleVital existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                DetalleVital actualizado = detalleVitalMapper.toEntity(dto);
                detalleVitalRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Detalle vital actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle vital ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle vital sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando detalle vital {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String detalleId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, detalleId, errorMessage);
        log.error("Error sincronizando detalle vital {}: {}", detalleId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String detalleId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(detalleId)
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
            } else if (message.contains("foreign key")) {
                return "Referencia a datos no existentes";
            } else if (message.contains("unique")) {
                return "Datos duplicados";
            }
        }
        return "Violación de restricción de base de datos";
    }
}
