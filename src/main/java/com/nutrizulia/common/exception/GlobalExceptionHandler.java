package com.nutrizulia.common.exception;

import com.nutrizulia.common.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getCurrentPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponseDto<Map<String, String>> response = ApiResponseDto.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Errores de validación")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error en el formato de los datos enviados")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Error de integridad de datos: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Error de integridad de datos")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Credenciales inválidas")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Recurso no encontrado")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Nuevos manejadores para excepciones personalizadas
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBusinessException(BusinessException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDto<String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> handleGenericException(Exception ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
