package com.nutrizulia.common.exception;

import com.nutrizulia.common.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String friendlyMessage = createFriendlyValidationMessage(error.getField(), error.getDefaultMessage());
            errors.put(error.getField(), friendlyMessage);
        });

        ApiResponseDto<Map<String, String>> response = ApiResponseDto.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Por favor, corrija los siguientes errores en el formulario:")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    private String createFriendlyValidationMessage(String fieldName, String originalMessage) {
        // Mapeo de nombres de campos técnicos a nombres amigables
        String friendlyFieldName = getFriendlyFieldName(fieldName);
        
        // Mapeo de mensajes de validación comunes
        if (originalMessage != null) {
            if (originalMessage.contains("must not be null") || originalMessage.contains("no debe ser nulo")) {
                return String.format("El campo '%s' es obligatorio.", friendlyFieldName);
            }
            if (originalMessage.contains("must not be blank") || originalMessage.contains("no debe estar vacío")) {
                return String.format("El campo '%s' no puede estar vacío.", friendlyFieldName);
            }
            if (originalMessage.contains("size must be between") || originalMessage.contains("el tamaño debe estar entre")) {
                return String.format("El campo '%s' debe tener la longitud correcta.", friendlyFieldName);
            }
            if (originalMessage.contains("must be a valid email") || originalMessage.contains("debe ser un email válido")) {
                return String.format("Por favor, ingrese un %s válido.", friendlyFieldName);
            }
            if (originalMessage.contains("must be a well-formed email address")) {
                return String.format("Por favor, ingrese un %s con formato válido (ejemplo@dominio.com).", friendlyFieldName);
            }
        }
        
        return originalMessage; // Si no hay mapeo específico, devolver el mensaje original
    }

    private String getFriendlyFieldName(String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "cedula" -> "número de cédula";
            case "nombres" -> "nombres";
            case "apellidos" -> "apellidos";
            case "fechanacimiento", "fecha_nacimiento" -> "fecha de nacimiento";
            case "genero" -> "género";
            case "telefono" -> "número de teléfono";
            case "correo", "email" -> "correo electrónico";
            case "clave", "password" -> "contraseña";
            case "usuario_id" -> "usuario";
            case "institucion_id" -> "institución";
            case "rol_id" -> "rol";
            default -> fieldName;
        };
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Los datos ingresados no cumplen con los requisitos. Por favor, verifique la información.")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Los datos enviados tienen un formato incorrecto. Por favor, verifique la información e intente nuevamente.")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        String friendlyMessage = createFriendlyDatabaseErrorMessage(ex.getMessage());
        
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message(friendlyMessage)
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String friendlyMessage = createFriendlyDatabaseErrorMessage(ex.getMessage());
        
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message(friendlyMessage)
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private String createFriendlyDatabaseErrorMessage(String originalMessage) {
        if (originalMessage == null) {
            return "No se pudo completar la operación debido a restricciones de datos.";
        }
        
        String lowerMessage = originalMessage.toLowerCase();
        
        if (lowerMessage.contains("duplicate entry") || lowerMessage.contains("unique constraint")) {
            if (lowerMessage.contains("cedula")) {
                return "Ya existe un usuario registrado con esta cédula. Por favor, verifique el número ingresado.";
            }
            if (lowerMessage.contains("correo") || lowerMessage.contains("email")) {
                return "Ya existe un usuario registrado con este correo electrónico. Por favor, utilice otro correo.";
            }
            if (lowerMessage.contains("telefono") || lowerMessage.contains("phone")) {
                return "Ya existe un usuario registrado con este número de teléfono. Por favor, utilice otro número.";
            }
            return "Ya existe un registro con esta información. Por favor, verifique los datos ingresados.";
        }
        
        if (lowerMessage.contains("foreign key constraint")) {
            return "No se puede completar la operación porque hay información relacionada que debe mantenerse.";
        }
        
        return "No se pudo completar la operación debido a restricciones de datos. Por favor, verifique la información ingresada.";
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
                .message("Las credenciales ingresadas son incorrectas. Por favor, verifique su cédula y contraseña.")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Nuevo: manejar cuentas deshabilitadas de forma amigable
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponseDto<String>> handleDisabledException(DisabledException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Su cuenta está deshabilitada. Por favor, contacte al administrador para reactivarla.")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("La página o recurso solicitado no se encuentra disponible.")
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
                .message(ex.getMessage()) // Los mensajes de BusinessException ya son amigables
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage()) // Los mensajes de ResourceNotFoundException ya son amigables
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDto<String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage()) // Los mensajes de DuplicateResourceException ya son amigables
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> handleGenericException(Exception ex) {
        ApiResponseDto<String> response = ApiResponseDto.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Ha ocurrido un error inesperado. Por favor, intente nuevamente o contacte al administrador del sistema.")
                .timestamp(LocalDateTime.now())
                .path(getCurrentPath())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
