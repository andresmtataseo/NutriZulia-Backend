package com.nutrizulia.features.collection.service.impl;

import com.nutrizulia.features.collection.dto.BatchSyncResponseDTO;
import com.nutrizulia.features.collection.dto.EvaluacionAntropometricaDto;
import com.nutrizulia.features.collection.dto.FullSyncResponseDTO;
import com.nutrizulia.features.collection.mapper.EvaluacionAntropometricaMapper;
import com.nutrizulia.features.collection.model.EvaluacionAntropometrica;
import com.nutrizulia.features.collection.repository.EvaluacionAntropometricaRepository;
import com.nutrizulia.features.collection.service.IEvaluacionAntropometricaService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluacionAntropometricaService implements IEvaluacionAntropometricaService {

    private final EvaluacionAntropometricaRepository evaluacionAntropometricaRepository;
    private final EvaluacionAntropometricaMapper evaluacionAntropometricaMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public BatchSyncResponseDTO syncEvaluacionesAntropometrica(List<EvaluacionAntropometricaDto> evaluacionesAntropometrica) {
        BatchSyncResponseDTO response = new BatchSyncResponseDTO();
        
        for (EvaluacionAntropometricaDto dto : evaluacionesAntropometrica) {
            try {
                procesarEvaluacionAntropometrica(dto, response);
            } catch (Exception e) {
                handleSyncError(dto.getId(), e, response);
            }
        }
        
        return response;
    }
    
    private void procesarEvaluacionAntropometrica(EvaluacionAntropometricaDto dto, BatchSyncResponseDTO response) {
        try {
            Optional<EvaluacionAntropometrica> existenteOpt = evaluacionAntropometricaRepository.findById(dto.getId());
            
            if (existenteOpt.isEmpty()) {
                crearNuevaEvaluacionAntropometrica(dto, response);
            } else {
                actualizarEvaluacionAntropometricaExistente(dto, existenteOpt.get(), response);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Error de integridad de datos para evaluación antropométrica {}: {}", dto.getId(), e.getMessage());
            agregarError(response, dto.getId(), "Error de integridad de datos: " + extractConstraintError(e));
        } catch (Exception e) {
            log.error("Error inesperado procesando evaluación antropométrica {}: {}", dto.getId(), e.getMessage(), e);
            agregarError(response, dto.getId(), "Error interno del servidor");
        }
    }
    
    private void crearNuevaEvaluacionAntropometrica(EvaluacionAntropometricaDto dto, BatchSyncResponseDTO response) {
        try {
            EvaluacionAntropometrica nueva = evaluacionAntropometricaMapper.toEntity(dto);
            evaluacionAntropometricaRepository.save(nueva);
            response.getSuccess().add(dto.getId());
            log.debug("Evaluación antropométrica creada exitosamente: {}", dto.getId());
        } catch (Exception e) {
            log.error("Error creando evaluación antropométrica {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void actualizarEvaluacionAntropometricaExistente(EvaluacionAntropometricaDto dto, EvaluacionAntropometrica existente, 
                                                           BatchSyncResponseDTO response) {
        try {
            if (dto.getUpdated_at().isAfter(existente.getUpdatedAt())) {
                EvaluacionAntropometrica actualizada = evaluacionAntropometricaMapper.toEntity(dto);
                evaluacionAntropometricaRepository.save(actualizada);
                response.getSuccess().add(dto.getId());
                log.debug("Evaluación antropométrica actualizada exitosamente: {}", dto.getId());
            } else if (dto.getUpdated_at().isBefore(existente.getUpdatedAt())) {
                // El servidor tiene la versión más reciente - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Evaluación antropométrica ya actualizada en servidor: {}", dto.getId());
            } else {
                // Versiones iguales - consideramos esto como éxito
                response.getSuccess().add(dto.getId());
                log.debug("Evaluación antropométrica sin cambios: {}", dto.getId());
            }
        } catch (Exception e) {
            log.error("Error actualizando evaluación antropométrica {}: {}", dto.getId(), e.getMessage());
            throw e;
        }
    }
    
    private void handleSyncError(String evaluacionId, Exception e, 
                               BatchSyncResponseDTO response) {
        String errorMessage = determinarMensajeError(e);
        agregarError(response, evaluacionId, errorMessage);
        log.error("Error sincronizando evaluación antropométrica {}: {}", evaluacionId, e.getMessage(), e);
    }
    
    private void agregarError(BatchSyncResponseDTO response, 
                            String evaluacionId, String reason) {
        BatchSyncResponseDTO.FailedRecordDTO failedRecord = BatchSyncResponseDTO.FailedRecordDTO.builder()
                .uuid(evaluacionId)
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
            if (message.contains("foreign key")) {
                return "Referencia a datos no existentes";
            } else if (message.contains("unique")) {
                return "Datos duplicados";
            }
        }
        return "Violación de restricción de base de datos";
    }

    @Override
    public FullSyncResponseDTO<EvaluacionAntropometricaDto> findAllActive() {
        log.info("Obteniendo todas las evaluaciones antropométricas activas para sincronización completa");

        // Obtener el usuario autenticado
        String cedula = getCurrentUserCedula();
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + cedula));

        // Obtener evaluaciones antropométricas filtradas por el usuario autenticado
        List<EvaluacionAntropometrica> evaluacionAntropometricas = evaluacionAntropometricaRepository.findAllActiveByUserId(usuario.getId());
        List<EvaluacionAntropometricaDto> evaluacionAntropometricaDtos = evaluacionAntropometricas.stream()
                .map(evaluacionAntropometricaMapper::toDto)
                .collect(Collectors.toList());

        log.info("Se encontraron {} evaluaciones antropometricas activas para el usuario {}", evaluacionAntropometricaDtos.size(), cedula);

        return FullSyncResponseDTO.<EvaluacionAntropometricaDto>builder()
                .tabla("evaluaciones_antropometricas")
                .totalRegistros(evaluacionAntropometricaDtos.size())
                .datos(evaluacionAntropometricaDtos)
                .build();
    }

    private String getCurrentUserCedula() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}
