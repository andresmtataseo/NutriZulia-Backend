package com.nutrizulia.features.catalog.service.impl;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.common.exception.ResourceNotFoundException;
import com.nutrizulia.common.util.ValidationUtils;
import com.nutrizulia.features.catalog.dto.InstitucionConUsuariosDto;
import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.mapper.InstitucionConUsuariosMapper;
import com.nutrizulia.features.catalog.mapper.InstitucionMapper;
import com.nutrizulia.features.catalog.model.Institucion;
import com.nutrizulia.features.catalog.repository.InstitucionRepository;
import com.nutrizulia.features.catalog.service.IInstitucionService;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class InstitucionService implements IInstitucionService {

    private final InstitucionRepository institucionRepository;
    private final InstitucionMapper institucionMapper;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;
    private final InstitucionConUsuariosMapper institucionConUsuariosMapper;

    @Override
    public List<InstitucionDto> getInstituciones() {
        log.debug("Obteniendo lista de todas las instituciones");
        try {
            List<InstitucionDto> instituciones = institucionRepository.findAll()
                    .stream()
                    .map(institucionMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Se obtuvieron {} instituciones exitosamente", instituciones.size());
            return instituciones;
        } catch (Exception e) {
            log.error("Error al obtener la lista de instituciones: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener las instituciones", e);
        }
    }

    @Override
    public InstitucionDto getInstitucionesById(Integer id) {
        log.debug("Obteniendo institución por ID: {}", id);
        ValidationUtils.validateId(id.longValue(), "ID de institución");
        
        try {
            return institucionRepository.findById(id)
                    .map(institucionMapper::toDto)
                    .orElseThrow(() -> new ResourceNotFoundException("Institución", "id", id));
        } catch (ResourceNotFoundException e) {
            log.warn("Institución no encontrada con ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener institución por ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al obtener la institución", e);
        }
    }

    @Override
    public PageResponseDto<InstitucionConUsuariosDto> getInstitucionConUsuarios(
            int page, int size, String search, String sortBy, String sortDir) {
        
        log.debug("Obteniendo instituciones con usuarios (activos e inactivos) - página: {}, tamaño: {}, búsqueda: '{}', ordenar por: {}, dirección: {}", 
                 page, size, search, sortBy, sortDir);
        
        try {
            // Validar campos de ordenamiento permitidos
            List<String> allowedSortFields = Arrays.asList("id", "nombre", "municipioSanitario.nombre", "tipoInstitucion.nombre");
            Pageable pageable = ValidationUtils.createPageable(page, size, sortBy, sortDir, allowedSortFields);
            
            String normalizedSearch = ValidationUtils.normalizeSearchTerm(search);
            
            // Obtener instituciones con paginación
            Page<Institucion> institucionesPage = searchInstitucionesWithDetails(normalizedSearch, pageable);
            
            if (institucionesPage.isEmpty()) {
                log.info("No se encontraron instituciones con los criterios especificados");
                return PageResponseDto.<InstitucionConUsuariosDto>builder()
                        .content(List.of())
                        .page(page)
                        .size(size)
                        .totalElements(0L)
                        .totalPages(0)
                        .first(true)
                        .last(true)
                        .empty(true)
                        .build();
            }
            
            // Obtener IDs de las instituciones
            List<Integer> institucionIds = institucionesPage.getContent()
                    .stream()
                    .map(Institucion::getId)
                    .collect(Collectors.toList());
            
            // Obtener todos los usuarios (activos e inactivos) para estas instituciones
            List<UsuarioInstitucion> usuariosInstituciones = usuarioInstitucionRepository
                    .findAllUsersByInstitucionIds(institucionIds);
            
            // Agrupar usuarios por institución
            Map<Integer, List<UsuarioInstitucion>> usuariosPorInstitucion = usuariosInstituciones
                    .stream()
                    .collect(Collectors.groupingBy(ui -> ui.getInstitucion().getId()));
            
            // Mapear a DTOs
            List<InstitucionConUsuariosDto> institucionesConUsuarios = institucionesPage.getContent()
                    .stream()
                    .map(institucion -> mapInstitucionConUsuarios(institucion, usuariosPorInstitucion.get(institucion.getId())))
                    .collect(Collectors.toList());
            
            // Crear página con los DTOs mapeados
            Page<InstitucionConUsuariosDto> resultPage = new PageImpl<>(
                    institucionesConUsuarios,
                    pageable,
                    institucionesPage.getTotalElements()
            );
            
            PageResponseDto<InstitucionConUsuariosDto> response = institucionConUsuariosMapper.toPageDto(resultPage);
            
            log.info("Se obtuvieron {} instituciones con usuarios (activos e inactivos) en la página {} de {}", 
                    institucionesConUsuarios.size(), page + 1, response.getTotalPages());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error al obtener instituciones con usuarios (activos e inactivos): {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener las instituciones con usuarios", e);
        }
    }

    /**
     * Busca instituciones con detalles según el término de búsqueda
     */
    private Page<Institucion> searchInstitucionesWithDetails(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            log.debug("Buscando instituciones con término: '{}'", search);
            return institucionRepository.findInstitucionesWithDetailsBySearch(search.trim(), pageable);
        } else {
            log.debug("Obteniendo todas las instituciones sin filtro de búsqueda");
            return institucionRepository.findAllInstitucionesWithDetails(pageable);
        }
    }

    /**
     * Mapea una institución con sus usuarios a DTO
     */
    private InstitucionConUsuariosDto mapInstitucionConUsuarios(Institucion institucion, List<UsuarioInstitucion> usuariosInstituciones) {
        List<InstitucionConUsuariosDto.UsuarioConRolDto> usuariosConRoles = List.of();
        
        if (usuariosInstituciones != null && !usuariosInstituciones.isEmpty()) {
            usuariosConRoles = usuariosInstituciones.stream()
                    .map(ui -> institucionConUsuariosMapper.toUsuarioConRolDto(ui.getUsuario(), ui))
                    .collect(Collectors.toList());
        }
        
        return institucionConUsuariosMapper.toDto(institucion, usuariosConRoles);
    }
}
