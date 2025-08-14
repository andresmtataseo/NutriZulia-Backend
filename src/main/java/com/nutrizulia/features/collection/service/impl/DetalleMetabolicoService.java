package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetalleMetabolicoDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.mapper.DetalleMetabolicoMapper;
import com.nutrizulia.features.collection.model.DetalleMetabolico;
import com.nutrizulia.features.collection.repository.DetalleMetabolicoRepository;
import com.nutrizulia.features.collection.service.IDetalleMetabolicoService;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DetalleMetabolicoService implements IDetalleMetabolicoService {

    private final DetalleMetabolicoRepository detalleMetabolicoRepository;
    private final DetalleMetabolicoMapper detalleMetabolicoMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

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
    
    @Override
    public FullSyncResponseDTO<DetalleMetabolicoDto> findAllActive() {
        log.info("Obteniendo todos los detalles metabólicos activos para sincronización completa");
        
        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));
        
        // Obtener las instituciones activas del usuario
        List<UsuarioInstitucion> institucionesActivas = usuarioInstitucionRepository
                .findActiveInstitutionsByUserId(usuario.getId());
        
        if (institucionesActivas.isEmpty()) {
            log.warn("El usuario {} no tiene instituciones activas", cedula);
            return FullSyncResponseDTO.<DetalleMetabolicoDto>builder()
                    .tabla("detalles_metabolicos")
                    .totalRegistros(0)
                    .datos(new ArrayList<>())
                    .build();
        }
        
        // Extraer los IDs de las instituciones activas
        List<Integer> institucionIds = institucionesActivas.stream()
                .map(ui -> ui.getInstitucion().getId())
                .toList();
        
        log.info("Filtrando detalles metabólicos para las instituciones: {}", institucionIds);
        
        // Obtener detalles metabólicos filtrados por instituciones activas del usuario
        List<DetalleMetabolico> detallesActivos = detalleMetabolicoRepository.findAllActiveByInstitutionIds(institucionIds);
        List<DetalleMetabolicoDto> detallesDto = detallesActivos.stream()
                .map(detalleMetabolicoMapper::toDto)
                .toList();
        
        log.info("Se encontraron {} detalles metabólicos activos para el usuario {}", detallesDto.size(), cedula);
        
        return FullSyncResponseDTO.<DetalleMetabolicoDto>builder()
                .tabla("detalles_metabolicos")
                .totalRegistros(detallesDto.size())
                .datos(detallesDto)
                .build();
    }
    
    private String getCurrentUserCedula() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // En este caso, el username es la cédula
        }
        throw new RuntimeException("No se pudo obtener el usuario autenticado");
    }
}
