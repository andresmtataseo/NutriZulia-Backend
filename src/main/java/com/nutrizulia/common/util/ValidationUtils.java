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
            throw new BusinessException("El número de página debe ser mayor o igual a cero", "INVALID_PAGE_NUMBER");
        }
        
        // Validar tamaño
        if (size <= 0) {
            throw new BusinessException("El número de elementos por página debe ser mayor a cero", "INVALID_PAGE_SIZE");
        }
        
        if (size > 100) {
            throw new BusinessException("No se pueden mostrar más de 100 elementos por página", "PAGE_SIZE_TOO_LARGE");
        }
        
        // Validar campo de ordenamiento
        if (sortBy != null && !allowedSortFields.contains(sortBy)) {
            throw new BusinessException(
                String.format("El campo de ordenamiento '%s' no es válido. Los campos disponibles son: %s", 
                    sortBy, String.join(", ", allowedSortFields)), 
                "INVALID_SORT_FIELD"
            );
        }
        
        // Validar dirección de ordenamiento
        if (sortDir != null && !Arrays.asList("asc", "desc").contains(sortDir.toLowerCase())) {
            throw new BusinessException(
                "La dirección de ordenamiento debe ser 'asc' (ascendente) o 'desc' (descendente)", 
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
                String.format("Por favor, proporcione un %s válido", fieldName), 
                "ID_REQUIRED"
            );
        }
        
        if (id <= 0) {
            throw new BusinessException(
                String.format("El %s debe ser un número válido mayor a cero", fieldName), 
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
                String.format("Por favor, complete el campo %s", fieldName), 
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