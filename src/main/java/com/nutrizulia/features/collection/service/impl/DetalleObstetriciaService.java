package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleObstetriciaDto;
import com.nutrizulia.features.collection.mapper.DetalleObstetriciaMapper;
import com.nutrizulia.features.collection.model.DetalleObstetricia;
import com.nutrizulia.features.collection.repository.DetalleObstetriciaRepository;
import com.nutrizulia.features.collection.service.IDetalleObstetriciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DetalleObstetriciaService implements IDetalleObstetriciaService {

    private final DetalleObstetriciaRepository detalleObstetriciaRepository;
    private final DetalleObstetriciaMapper detalleObstetriciaMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDetallesObstetricia(List<DetalleObstetriciaDto> detallesObstetricias) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DetalleObstetriciaDto dto : detallesObstetricias) {
            try {
                procesarDetalleObstetricia(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDetalleObstetricia(DetalleObstetriciaDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<DetalleObstetricia> existenteOpt = detalleObstetriciaRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDetalleObstetricia(dto, response);
            } else {
                actualizarDetalleObstetriciaExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para detalle obstetricia {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando detalle obstetricia {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDetalleObstetricia(DetalleObstetriciaDto dto, BatchSyncResponseDTO response) {
        try {
            DetalleObstetricia nuevo = detalleObstetriciaMapper.toEntity(dto);
            detalleObstetriciaRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Detalle obstetricia creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando detalle obstetricia {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDetalleObstetriciaExistente(DetalleObstetriciaDto dto, DetalleObstetricia existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                DetalleObstetricia actualizado = detalleObstetriciaMapper.toEntity(dto);
                detalleObstetriciaRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Detalle obstetricia actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle obstetricia ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle obstetricia sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando detalle obstetricia {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String detalleId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, detalleId, errorMessage);
        log.error("Error sincronizando detalle obstetricia {}: {}", detalleId, e.getMessage(), e);
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
