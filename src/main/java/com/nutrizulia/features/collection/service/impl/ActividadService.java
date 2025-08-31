package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.model.Actividad;
import com.nutrizulia.features.collection.mapper.ActividadMapper;
import com.nutrizulia.features.collection.repository.ActividadRepository;
import com.nutrizulia.features.collection.service.IActividadService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ActividadService implements IActividadService {

    private final ActividadRepository actividadRepository;
    private final ActividadMapper actividadMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

    @Override
    public List<ActividadDto> getActividades() {
        return actividadRepository.findAll().stream().map(actividadMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BatchSyncResponseDTO sycnActividades(List<ActividadDto> actividades) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (ActividadDto dto : actividades) {
            try {
                procesarActividad(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }

    @Override
    public FullSyncResponseDTO<ActividadDto> findAllActive() {
        log.info("Obteniendo todas las actividades activas para sincronización completa");

        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));

        // Obtener las instituciones activas del usuario
        List<UsuarioInstitucion> institucionesActivas = usuarioInstitucionRepository
                .findActiveInstitutionsByUserId(usuario.getId());

        if (institucionesActivas.isEmpty()) {
            log.warn("El usuario {} no tiene instituciones activas", cedula);
            return FullSyncResponseDTO.<ActividadDto>builder()
                    .tabla("actividades")
                    .totalRegistros(0)
                    .datos(new ArrayList<>())
                    .build();
        }

        // Extraer los IDs de las instituciones activas
        List<Integer> institucionIds = institucionesActivas.stream()
                .map(ui -> ui.getInstitucion().getId())
                .collect(Collectors.toList());

        log.info("Filtrando actividades para las instituciones: {}", institucionIds);

        // Obtener consultas filtradas por instituciones activas del usuario
        List<Actividad> actividadesActivas = actividadRepository.findAllActiveByInstitutionIds(institucionIds);
        List<ActividadDto> actividadesDto = actividadesActivas.stream()
                .map(actividadMapper::toDto)
                .collect(Collectors.toList());

        log.info("Se encontraron {} actividades activas para el usuario {}", actividadesDto.size(), cedula);

        return FullSyncResponseDTO.<ActividadDto>builder()
                .tabla("actividades")
                .totalRegistros(actividadesDto.size())
                .datos(actividadesDto)
                .build();
    }

    private String getCurrentUserCedula() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    private void procesarActividad(ActividadDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<Actividad> existenteOpt = actividadRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevaActividad(dto, response);
            } else {
                actualizarActividadExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para actividad {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando actividad {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevaActividad(ActividadDto dto, BatchSyncResponseDTO response) {
        try {
            Actividad nueva = actividadMapper.toEntity(dto);
            log.debug("Creando actividad con id (MAPEO): {}", nueva);
            actividadRepository.save(nueva);
            response.getSuccess().add(dto.getId());
            log.debug("Actividad creada exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando actividad {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarActividadExistente(ActividadDto dto, Actividad existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                Actividad actualizada = actividadMapper.toEntity(dto);
                actividadRepository.save(actualizada);
                response.getSuccess().add(dto.getId());
                log.debug("Actividad actualizada exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Actividad ya actualizada en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Actividad sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando actividad {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String actividadId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, actividadId, errorMessage);
        log.error("Error sincronizando actividad {}: {}", actividadId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String actividadId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(actividadId)
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
