package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteDto;
import com.nutrizulia.features.collection.dto.RepresentanteDto;
import com.nutrizulia.features.collection.mapper.RepresentanteMapper;
import com.nutrizulia.features.collection.model.Representante;
import com.nutrizulia.features.collection.repository.RepresentanteRepository;
import com.nutrizulia.features.collection.service.IRepresentanteService;
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
public class RepresentanteService implements IRepresentanteService {

    private final RepresentanteRepository representanteRepository;
    private final RepresentanteMapper representanteMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncRepresentantes(List<RepresentanteDto> representantes) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (RepresentanteDto dto : representantes) {
            try {
                procesarRepresentante(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarRepresentante(RepresentanteDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<Representante> existenteOpt = representanteRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoRepresentante(dto, response);
            } else {
                actualizarRepresentanteExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para representante {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando representante {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoRepresentante(RepresentanteDto dto, BatchSyncResponseDTO response) {
        try {
            Representante nuevo = representanteMapper.toEntity(dto);
            representanteRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Representante creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando representante {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarRepresentanteExistente(RepresentanteDto dto, Representante existente, 
                                                 BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                Representante actualizado = representanteMapper.toEntity(dto);
                representanteRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Representante actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Representante ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Representante sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando representante {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String representanteId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, representanteId, errorMessage);
        log.error("Error sincronizando representante {}: {}", representanteId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String representanteId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(representanteId)
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
    public FullSyncResponseDTO<RepresentanteDto> findAllActive() {
        log.info("Iniciando sincronización completa de representantes");
        
        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        log.debug("Usuario autenticado: {}", cedula);
        
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));
        
        // Obtener las instituciones activas del usuario
        List<UsuarioInstitucion> usuarioInstituciones = usuarioInstitucionRepository
                .findActiveInstitutionsByUserId(usuario.getId());
        
        if (usuarioInstituciones.isEmpty()) {
            log.warn("Usuario {} no tiene instituciones activas asignadas", cedula);
            return FullSyncResponseDTO.<RepresentanteDto>builder()
                    .tabla("representantes")
                    .totalRegistros(0)
                    .datos(new ArrayList<>())
                    .build();
        }
        
        // Extraer los IDs de las instituciones
        List<Integer> institutionIds = usuarioInstituciones.stream()
                .map(ui -> ui.getInstitucion().getId())
                .toList();
        
        log.debug("Filtrando representantes por instituciones: {}", institutionIds);
        
        // Obtener representantes activos filtrados por las instituciones del usuario
        List<Representante> representantes = representanteRepository
                .findAllActiveByInstitutionIds(institutionIds);
        
        // Mapear a DTOs
        List<RepresentanteDto> representanteDtos = representantes.stream()
                .map(representanteMapper::toDto)
                .toList();
        
        log.info("Sincronización completa completada: {} representantes activos encontrados", 
                representanteDtos.size());
        
        return FullSyncResponseDTO.<RepresentanteDto>builder()
                .tabla("representantes")
                .totalRegistros(representanteDtos.size())
                .datos(representanteDtos)
                .build();
    }
    
    private String getCurrentUserCedula() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // La cédula se almacena como username
        }
        throw new RuntimeException("No se pudo obtener el usuario autenticado");
    }
}
