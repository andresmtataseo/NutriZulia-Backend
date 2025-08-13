package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleAntropometricoDto;
import com.nutrizulia.features.collection.mapper.DetalleAntropometricoMapper;
import com.nutrizulia.features.collection.model.DetalleAntropometrico;
import com.nutrizulia.features.collection.repository.DetalleAntropometricoRepository;
import com.nutrizulia.features.collection.service.IDetalleAntropometricoService;
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
public class DetalleAntropometricoService implements IDetalleAntropometricoService {

    private final DetalleAntropometricoRepository detalleAntropometricoRepository;
    private final DetalleAntropometricoMapper detalleAntropometricoMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDetallesAntropometrico(List<DetalleAntropometricoDto> detallesAntropometricos) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DetalleAntropometricoDto dto : detallesAntropometricos) {
            try {
                procesarDetalleAntropometrico(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDetalleAntropometrico(DetalleAntropometricoDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<DetalleAntropometrico> existenteOpt = detalleAntropometricoRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDetalleAntropometrico(dto, response);
            } else {
                actualizarDetalleAntropometricoExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para detalle antropométrico {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando detalle antropométrico {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDetalleAntropometrico(DetalleAntropometricoDto dto, BatchSyncResponseDTO response) {
        try {
            DetalleAntropometrico nuevo = detalleAntropometricoMapper.toEntity(dto);
            detalleAntropometricoRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Detalle antropométrico creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando detalle antropométrico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDetalleAntropometricoExistente(DetalleAntropometricoDto dto, DetalleAntropometrico existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                DetalleAntropometrico actualizado = detalleAntropometricoMapper.toEntity(dto);
                detalleAntropometricoRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Detalle antropométrico actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle antropométrico ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle antropométrico sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando detalle antropométrico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String detalleId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, detalleId, errorMessage);
        log.error("Error sincronizando detalle antropométrico {}: {}", detalleId, e.getMessage(), e);
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
