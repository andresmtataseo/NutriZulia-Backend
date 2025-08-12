package com.nutrizulia.features.user.service;

import com.nutrizulia.common.exception.BusinessException;
import com.nutrizulia.common.exception.DuplicateResourceException;
import com.nutrizulia.common.exception.ResourceNotFoundException;
import com.nutrizulia.features.catalog.model.Rol;
import com.nutrizulia.features.catalog.repository.InstitucionRepository;
import com.nutrizulia.features.catalog.repository.RolRepository;
import com.nutrizulia.features.user.dto.CreateUsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UpdateUsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.mapper.UsuarioInstitucionMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioInstitucionService implements IUsuarioInstitucionService {

    private final UsuarioInstitucionRepository usuarioInstitucionRepository;
    private final UsuarioRepository usuarioRepository;
    private final InstitucionRepository institucionRepository;
    private final RolRepository rolRepository;
    private final UsuarioInstitucionMapper usuarioInstitucionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioInstitucionDto> getUsuarioInstitucionesByUsuarioId(Integer usuarioId) {
        
        List<UsuarioInstitucion> usuarioInstituciones = usuarioInstitucionRepository.findAllByUsuario_Id(usuarioId);
        
        return usuarioInstituciones.stream()
                .map(usuarioInstitucionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioInstitucionDto createUsuarioInstitucion(CreateUsuarioInstitucionDto createDto) {
        log.debug("Creando nueva asignación usuario-institución: {}", createDto);

        // 1. Validar que no exista una asignación activa duplicada
        validateDuplicateAssignment(createDto);

        // 2. Validar que existan las entidades relacionadas
        validateRelatedEntities(createDto);

        // 3. Validar que el usuario esté activo si se intenta crear una asignación activa
        validateUserActiveForActiveAssignment(createDto.getUsuario_id(), true);

        // 4. Validar restricción de roles administrativos
        validateAdministrativeRoleRestriction(createDto.getRol_id(), createDto.getInstitucion_id());

        // 5. Crear la entidad con fechas automáticas
        UsuarioInstitucion usuarioInstitucion = usuarioInstitucionMapper.toEntity(createDto);
        
        // Asignar fecha de inicio automáticamente (fecha actual)
        usuarioInstitucion.setFechaInicio(LocalDate.now());
        
        // La fecha de fin se deja null (sin fecha de finalización)
        usuarioInstitucion.setFechaFin(null);

        // 5. Guardar la nueva asignación
        UsuarioInstitucion savedUsuarioInstitucion = usuarioInstitucionRepository.save(usuarioInstitucion);

        log.info("Asignación usuario-institución creada exitosamente con ID: {} - Fecha inicio: {}", 
                savedUsuarioInstitucion.getId(), savedUsuarioInstitucion.getFechaInicio());
        
        return usuarioInstitucionMapper.toDto(savedUsuarioInstitucion);
    }

    @Override
    @Transactional
    public UsuarioInstitucionDto updateUsuarioInstitucion(Integer id, UpdateUsuarioInstitucionDto updateDto) {
        log.debug("Actualizando asignación usuario-institución con ID: {} - Datos: {}", id, updateDto);

        // 1. Buscar la asignación existente
        UsuarioInstitucion existingAssignment = usuarioInstitucionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioInstitucion", "id", id.toString()));

        // 2. Validar que existe el nuevo rol
        if (!rolRepository.existsById(updateDto.getRol_id())) {
            throw new ResourceNotFoundException("Rol", "id", updateDto.getRol_id().toString());
        }

        // 3. Validar que el usuario esté activo si se intenta activar la asignación
        if (updateDto.getIs_enabled()) {
            validateUserActiveForActiveAssignment(existingAssignment.getUsuario().getId(), updateDto.getIs_enabled());
        }

        // 4. Validar restricción de roles administrativos
        validateAdministrativeRoleRestriction(updateDto.getRol_id(), existingAssignment.getInstitucion().getId());

        // 5. Actualizar los campos
        updateAssignmentFields(existingAssignment, updateDto);

        // 6. Guardar los cambios
        UsuarioInstitucion updatedAssignment = usuarioInstitucionRepository.save(existingAssignment);

        log.info("Asignación usuario-institución actualizada exitosamente con ID: {} - Rol: {} - Estado: {} - Fecha fin: {}", 
                updatedAssignment.getId(), updatedAssignment.getRol().getId(), updatedAssignment.getIsEnabled(),
                updatedAssignment.getFechaFin());
        
        return usuarioInstitucionMapper.toDto(updatedAssignment);
    }

    private void validateDuplicateAssignment(CreateUsuarioInstitucionDto createDto) {
        // Validar que el usuario no tenga ya un rol activo en esta institución
        boolean userHasActiveRoleInInstitution = usuarioInstitucionRepository.existsActiveUserInInstitution(
                createDto.getUsuario_id(),
                createDto.getInstitucion_id()
        );

        if (userHasActiveRoleInInstitution) {
            throw new DuplicateResourceException(
                    "UsuarioInstitucion",
                    "usuario_id, institucion_id",
                    String.format("El usuario con ID %d ya tiene un rol activo en la institución con ID %d. " +
                            "Un usuario solo puede tener un rol por institución.", 
                            createDto.getUsuario_id(), createDto.getInstitucion_id())
            );
        }
    }

    private void validateRelatedEntities(CreateUsuarioInstitucionDto createDto) {
        // Validar que existe el usuario
        if (!usuarioRepository.existsById(createDto.getUsuario_id())) {
            throw new ResourceNotFoundException("Usuario", "id", createDto.getUsuario_id().toString());
        }

        // Validar que existe la institución
        if (!institucionRepository.existsById(createDto.getInstitucion_id())) {
            throw new ResourceNotFoundException("Institucion", "id", createDto.getInstitucion_id().toString());
        }

        // Validar que existe el rol
        if (!rolRepository.existsById(createDto.getRol_id())) {
            throw new ResourceNotFoundException("Rol", "id", createDto.getRol_id().toString());
        }
    }

    /**
     * Valida que el usuario esté activo cuando se intenta crear o activar una asignación
     */
    private void validateUserActiveForActiveAssignment(Integer usuarioId, Boolean isEnabled) {
        if (isEnabled != null && isEnabled) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId.toString()));
            
            if (usuario.getIsEnabled() == null || !usuario.getIsEnabled()) {
                throw new BusinessException(
                        "No es posible asignar una institución a un usuario que se encuentra inactivo. " +
                        "Para continuar, primero debe activar el usuario en el sistema y luego proceder con la asignación.",
                        "USER_INACTIVE_FOR_ASSIGNMENT"
                );
            }
        }
    }

    /**
     * Valida que los roles administrativos (ID 1 y 2) solo puedan ser asignados a la institución con ID 1
     */
    private void validateAdministrativeRoleRestriction(Integer rolId, Integer institucionId) {
        // Los roles con ID 1 y 2 son administrativos y solo pueden ser asignados a la institución con ID 1
        if ((rolId == 1 || rolId == 2) && !institucionId.equals(1)) {
            throw new BusinessException(
                    "Los roles administrativos de la Coordinación Regional de Nutrición y Dietética " +
                    "solo pueden ser asignados a usuarios de la institución principal. " +
                    "Por favor, seleccione un rol diferente para esta institución.",
                    "ADMINISTRATIVE_ROLE_RESTRICTION"
            );
        }
    }

    /**
     * Actualiza los campos de una asignación usuario-institución y maneja automáticamente las fechas
     */
    private void updateAssignmentFields(UsuarioInstitucion existingAssignment, UpdateUsuarioInstitucionDto updateDto) {
        // Buscar y asignar el nuevo rol
        Rol newRol = rolRepository.findById(updateDto.getRol_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", updateDto.getRol_id().toString()));
        existingAssignment.setRol(newRol);
        
        // Actualizar el estado y manejar fechas automáticamente
        existingAssignment.setIsEnabled(updateDto.getIs_enabled());
        updateAssignmentDates(existingAssignment, updateDto.getIs_enabled());
    }

    /**
     * Maneja automáticamente las fechas de fin según el estado de la asignación
     */
    private void updateAssignmentDates(UsuarioInstitucion assignment, Boolean isEnabled) {
        if (isEnabled != null) {
            if (isEnabled) {
                // Si se activa la asignación, quitar la fecha de fin
                assignment.setFechaFin(null);
                log.debug("Asignación activada - Fecha de fin removida para ID: {}", assignment.getId());
            } else {
                // Si se desactiva la asignación, establecer la fecha de fin como hoy
                assignment.setFechaFin(LocalDate.now());
                log.debug("Asignación desactivada - Fecha de fin establecida para ID: {} - Fecha: {}", 
                        assignment.getId(), assignment.getFechaFin());
            }
        }
    }
}