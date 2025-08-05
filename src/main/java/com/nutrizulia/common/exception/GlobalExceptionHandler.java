package com.nutrizulia.common.exception;

import com.nutrizulia.common.dto.ApiResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Validaciones con @Valid fallidas
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing // evitar colisiones
                ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación en los campos de entrada.", request, null, errors);
    }

    // ✅ Validaciones fallidas con @Validated (por ejemplo en @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación de parámetros.", request, null, errors);
    }

    // ✅ JSON malformado, tipo incorrecto en el cuerpo, etc.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud contiene errores de formato o tipo.", request, null, null);
    }

    // ✅ Excepciones de integridad (como claves únicas duplicadas)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Violación de integridad de datos. Posible valor duplicado o inconsistente.", request, null, null);
    }

    // ✅ Errores personalizados tipo IllegalArgument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null, null);
    }

    // ✅ Credenciales inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas. Verifique su cédula y contraseña.", request, null, null);
    }

    // ✅ Ruta no encontrada
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotFound(NoResourceFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado.", request, null, null);
    }

    // ✅ Errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGlobal(Exception ex, WebRequest request) {
        ex.printStackTrace(); // solo en dev, en prod usar logger
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado del servidor. Intente más tarde.", request, null, null);
    }

    //  Metodo reutilizable para construir respuestas
    private <T> ResponseEntity<ApiResponseDto<T>> buildResponse(
            HttpStatus status,
            String message,
            WebRequest request,
            T data,
            Map<String, String> errors
    ) {
        ApiResponseDto<T> response = ApiResponseDto.<T>builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .data(data)
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
