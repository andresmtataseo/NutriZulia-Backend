package com.nutrizulia.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Puedes añadir más @ExceptionHandler para otras excepciones comunes si lo necesitas
    // Por ejemplo, para EntityNotFoundException:
     @ExceptionHandler(EntityNotFoundException.class)
     public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
         return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
     }

    // O un manejador general para cualquier otra excepción no controlada
     @ExceptionHandler(Exception.class)
     public ResponseEntity<String> handleGenericException(Exception ex) {
         return new ResponseEntity<>("Ocurrió un error inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
}