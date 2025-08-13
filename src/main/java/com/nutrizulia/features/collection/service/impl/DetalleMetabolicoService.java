package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.mapper.DetalleMetabolicoMapper;
import com.nutrizulia.features.collection.model.DetalleMetabolico;
import com.nutrizulia.features.collection.repository.DetalleMetabolicoRepository;
import com.nutrizulia.features.collection.service.IDetalleMetabolicoService;
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
public class DetalleMetabolicoService implements IDetalleMetabolicoService {

    private final DetalleMetabolicoRepository detalleMetabolicoRepository;
    private final DetalleMetabolicoMapper detalleMetabolicoMapper;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDetallesMetabolico(List<DetalleMetabolicoDto> detallesMetabolicos) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DetalleMetabolicoDto dto : detallesMetabolicos) {
            try {
                procesarDetalleMetabolico(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDetalleMetabolico(DetalleMetabolicoDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<DetalleMetabolico> existenteOpt = detalleMetabolicoRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDetalleMetabolico(dto, response);
            } else {
                actualizarDetalleMetabolicoExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para detalle metabólico {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando detalle metabólico {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDetalleMetabolico(DetalleMetabolicoDto dto, BatchSyncResponseDTO response) {
        try {
            DetalleMetabolico nuevo = detalleMetabolicoMapper.toEntity(dto);
            detalleMetabolicoRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Detalle metabólico creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando detalle metabólico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDetalleMetabolicoExistente(DetalleMetabolicoDto dto, DetalleMetabolico existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                DetalleMetabolico actualizado = detalleMetabolicoMapper.toEntity(dto);
                detalleMetabolicoRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Detalle metabólico actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle metabólico ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle metabólico sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando detalle metabólico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String detalleId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, detalleId, errorMessage);
        log.error("Error sincronizando detalle metabólico {}: {}", detalleId, e.getMessage(), e);
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
