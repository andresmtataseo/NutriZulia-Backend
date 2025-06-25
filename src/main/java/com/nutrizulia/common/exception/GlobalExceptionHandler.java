package com.nutrizulia.common.exception;

import com.nutrizulia.common.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiResponseDto apiError = ApiResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación en los campos de entrada.")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiResponseDto apiError = ApiResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiResponseDto apiError = ApiResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Credenciales de autenticación inválidas. Verifique su email y contraseña.")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // ✅ Manejador para rutas que no existen (404)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        ApiResponseDto apiError = ApiResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Recurso no encontrado.")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // ✅ Manejador genérico para errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        System.err.println("Error inesperado: " + ex.getClass().getName() + " - " + ex.getMessage());
        ex.printStackTrace();

        ApiResponseDto apiError = ApiResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Ocurrió un error inesperado en el servidor. Por favor, inténtelo de nuevo más tarde.")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
