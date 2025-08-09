package com.nutrizulia.common.util;

import com.nutrizulia.common.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

/**
 * Utilidades de validación comunes para el proyecto
 */
public class ValidationUtils {
    
    private ValidationUtils() {
        // Clase de utilidades - constructor privado
    }
    
    /**
     * Valida y crea un objeto Pageable con parámetros seguros
     */
    public static Pageable createPageable(int page, int size, String sortBy, String sortDir, List<String> allowedSortFields) {
        // Validar página
        if (page < 0) {
            throw new BusinessException("El número de página no puede ser negativo", "INVALID_PAGE_NUMBER");
        }
        
        // Validar tamaño
        if (size <= 0) {
            throw new BusinessException("El tamaño de página debe ser mayor a 0", "INVALID_PAGE_SIZE");
        }
        
        if (size > 100) {
            throw new BusinessException("El tamaño de página no puede ser mayor a 100", "PAGE_SIZE_TOO_LARGE");
        }
        
        // Validar campo de ordenamiento
        if (sortBy != null && !allowedSortFields.contains(sortBy)) {
            throw new BusinessException(
                String.format("Campo de ordenamiento inválido: %s. Campos permitidos: %s", 
                    sortBy, String.join(", ", allowedSortFields)), 
                "INVALID_SORT_FIELD"
            );
        }
        
        // Validar dirección de ordenamiento
        if (sortDir != null && !Arrays.asList("asc", "desc").contains(sortDir.toLowerCase())) {
            throw new BusinessException(
                "Dirección de ordenamiento inválida. Use 'asc' o 'desc'", 
                "INVALID_SORT_DIRECTION"
            );
        }
        
        // Crear Sort
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            sort = sortDir != null && sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        }
        
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * Valida que un ID no sea nulo o negativo
     */
    public static void validateId(Long id, String fieldName) {
        if (id == null) {
            throw new BusinessException(
                String.format("El %s es obligatorio", fieldName), 
                "ID_REQUIRED"
            );
        }
        
        if (id <= 0) {
            throw new BusinessException(
                String.format("El %s debe ser mayor a 0", fieldName), 
                "INVALID_ID"
            );
        }
    }
    
    /**
     * Valida que una cadena no sea nula o vacía
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(
                String.format("El campo %s es obligatorio", fieldName), 
                "FIELD_REQUIRED"
            );
        }
    }
    
    /**
     * Normaliza un término de búsqueda
     */
    public static String normalizeSearchTerm(String search) {
        if (search == null) {
            return null;
        }
        
        String normalized = search.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}