package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.dto.PacienteDto;
import com.nutrizulia.features.collection.mapper.PacienteMapper;
import com.nutrizulia.features.collection.model.Paciente;
import com.nutrizulia.features.collection.repository.PacienteRepository;
import com.nutrizulia.features.collection.service.IPacienteService;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PacienteService implements IPacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncPacientes(List<PacienteDto> pacientes) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (PacienteDto dto : pacientes) {
            try {
                procesarPaciente(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarPaciente(PacienteDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<Paciente> existenteOpt = pacienteRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevoPaciente(dto, response);
            } else {
                actualizarPacienteExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para paciente {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando paciente {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevoPaciente(PacienteDto dto, BatchSyncResponseDTO response) {
        try {
            Paciente nuevo = pacienteMapper.toEntity(dto);
            pacienteRepository.save(nuevo);
            response.getSuccess().add(dto.getId());
            log.debug("Paciente creado exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando paciente {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarPacienteExistente(PacienteDto dto, Paciente existente, 
                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                Paciente actualizado = pacienteMapper.toEntity(dto);
                pacienteRepository.save(actualizado);
                response.getSuccess().add(dto.getId());
                log.debug("Paciente actualizado exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Paciente ya actualizado en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Paciente sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando paciente {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String pacienteId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, pacienteId, errorMessage);
        log.error("Error sincronizando paciente {}: {}", pacienteId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String pacienteId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(pacienteId)
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
    
    @Override
    public FullSyncResponseDTO<PacienteDto> findAllActive() {
        log.info("Obteniendo pacientes activos pertenecientes al usuario autenticado");
        
        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));
        
        // Filtrar pacientes pertenecientes al usuario autenticado
        log.debug("Filtrando pacientes por usuario: {}", usuario.getId());
        List<Paciente> pacientesActivos = pacienteRepository.findAllActiveByUserId(usuario.getId());
        List<PacienteDto> pacientesDto = pacientesActivos.stream()
                .map(pacienteMapper::toDto)
                .toList();
        
        log.info("Se encontraron {} pacientes activos pertenecientes al usuario {}", pacientesDto.size(), cedula);
        
        return FullSyncResponseDTO.<PacienteDto>builder()
                .tabla("pacientes")
                .totalRegistros(pacientesDto.size())
                .datos(pacientesDto)
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
