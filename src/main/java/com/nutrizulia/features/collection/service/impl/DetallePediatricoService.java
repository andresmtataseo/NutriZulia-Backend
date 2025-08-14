package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.DetallePedriatricoDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.mapper.DetallePediatricoMapper;
import com.nutrizulia.features.collection.model.DetallePediatrico;
import com.nutrizulia.features.collection.repository.DetallePediatricoRepository;
import com.nutrizulia.features.collection.service.IDetallePediatricoService;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import com.nutrizulia.features.user.repository.UsuarioRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class DetallePediatricoService implements IDetallePediatricoService {

    private final DetallePediatricoRepository detallePediatricoRepository;
    private final DetallePediatricoMapper detallePediatricoMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncDetallesPediatrico(List<DetallePedriatricoDto> detallesPediatricos) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (DetallePedriatricoDto dto : detallesPediatricos) {
            try {
                procesarDetallePediatrico(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarDetallePediatrico(DetallePedriatricoDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<DetallePediatrico> existenteOpt = detallePediatricoRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoDetallePediatrico(dto, response);
            } else {
                actualizarDetallePediatricoExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para detalle pediátrico {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando detalle pediátrico {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoDetallePediatrico(DetallePedriatricoDto dto, BatchSyncResponseDTO response) {
        try {
            DetallePediatrico nuevo = detallePediatricoMapper.toEntity(dto);
            detallePediatricoRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Detalle pediátrico creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando detalle pediátrico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarDetallePediatricoExistente(DetallePedriatricoDto dto, DetallePediatrico existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                DetallePediatrico actualizado = detallePediatricoMapper.toEntity(dto);
                detallePediatricoRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Detalle pediátrico actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle pediátrico ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Detalle pediátrico sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando detalle pediátrico {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String detalleId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, detalleId, errorMessage);
        log.error("Error sincronizando detalle pediátrico {}: {}", detalleId, e.getMessage(), e);
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
    public FullSyncResponseDTO<DetallePedriatricoDto> findAllActive() {
        log.info("Obteniendo todos los detalles pediátricos activos para sincronización completa");
        
        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));
        
        // Obtener las instituciones activas del usuario
        List<UsuarioInstitucion> institucionesActivas = usuarioInstitucionRepository
                .findActiveInstitutionsByUserId(usuario.getId());
        
        if (institucionesActivas.isEmpty()) {
            log.warn("El usuario {} no tiene instituciones activas", cedula);
            return FullSyncResponseDTO.<DetallePedriatricoDto>builder()
                    .tabla("detalles_pediatricos")
                    .totalRegistros(0)
                    .datos(new ArrayList<>())
                    .build();
        }
        
        // Extraer los IDs de las instituciones activas
        List<Integer> institucionIds = institucionesActivas.stream()
                .map(ui -> ui.getInstitucion().getId())
                .toList();
        
        log.info("Filtrando detalles pediátricos para las instituciones: {}", institucionIds);
        
        // Obtener detalles pediátricos filtrados por instituciones activas del usuario
        List<DetallePediatrico> detallesActivos = detallePediatricoRepository.findAllActiveByInstitutionIds(institucionIds);
        List<DetallePedriatricoDto> detallesDto = detallesActivos.stream()
                .map(detallePediatricoMapper::toDto)
                .toList();
        
        log.info("Se encontraron {} detalles pediátricos activos para el usuario {}", detallesDto.size(), cedula);
        
        return FullSyncResponseDTO.<DetallePedriatricoDto>builder()
                .tabla("detalles_pediatricos")
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
