package com.nutrizulia.common.validator;

import com.nutrizulia.common.enums.Genero;
import com.nutrizulia.common.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Validador centralizado para datos de usuario
 */
@Component
public class UserValidator {
    
    // Patrones de validación
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^[VE]-\\d{7,8}$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^(0414|0424|0412|0416|0426)-\\d{7}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Constantes de validación
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 150;
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    /**
     * Valida el formato de la cédula
     */
    public void validateCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new BusinessException("La cédula es obligatoria", "CEDULA_REQUIRED");
        }
        
        if (!CEDULA_PATTERN.matcher(cedula).matches()) {
            throw new BusinessException(
                "Formato de cédula inválido. Debe ser V-12345678 o E-12345678", 
                "CEDULA_INVALID_FORMAT"
            );
        }
    }
    
    /**
     * Valida el formato del teléfono
     */
    public void validateTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!TELEFONO_PATTERN.matcher(telefono).matches()) {
                throw new BusinessException(
                    "Formato de teléfono inválido. Debe ser 04XX-XXXXXXX (ejemplo: 0424-6719783)", 
                    "TELEFONO_INVALID_FORMAT"
                );
            }
        }
    }
    
    /**
     * Valida el formato del correo electrónico
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El correo electrónico es obligatorio", "EMAIL_REQUIRED");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(
                "Formato de correo electrónico inválido", 
                "EMAIL_INVALID_FORMAT"
            );
        }
    }
    
    /**
     * Valida el género
     */
    public void validateGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new BusinessException("El género es obligatorio", "GENERO_REQUIRED");
        }
        
        try {
            Genero.valueOf(genero.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(
                "Género inválido. Los valores permitidos son: MASCULINO, FEMENINO", 
                "GENERO_INVALID"
            );
        }
    }
    
    /**
     * Valida la fecha de nacimiento
     */
    public void validateFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria", "FECHA_NACIMIENTO_REQUIRED");
        }
        
        LocalDate fechaMinima = LocalDate.now().minusYears(MIN_AGE);
        if (fechaNacimiento.isAfter(fechaMinima)) {
            throw new BusinessException(
                String.format("El usuario debe ser mayor de %d años", MIN_AGE), 
                "EDAD_MINIMA_NO_CUMPLIDA"
            );
        }
        
        LocalDate fechaMaxima = LocalDate.now().minusYears(MAX_AGE);
        if (fechaNacimiento.isBefore(fechaMaxima)) {
            throw new BusinessException(
                "Fecha de nacimiento inválida", 
                "FECHA_NACIMIENTO_INVALIDA"
            );
        }
    }
    
    /**
     * Valida la contraseña
     */
    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria", "PASSWORD_REQUIRED");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BusinessException(
                String.format("La contraseña debe tener al menos %d caracteres", MIN_PASSWORD_LENGTH), 
                "PASSWORD_TOO_SHORT"
            );
        }
        
        // Validaciones adicionales de complejidad de contraseña
        if (!hasUpperCase(password)) {
            throw new BusinessException(
                "La contraseña debe contener al menos una letra mayúscula", 
                "PASSWORD_NO_UPPERCASE"
            );
        }
        
        if (!hasLowerCase(password)) {
            throw new BusinessException(
                "La contraseña debe contener al menos una letra minúscula", 
                "PASSWORD_NO_LOWERCASE"
            );
        }
        
        if (!hasDigit(password)) {
            throw new BusinessException(
                "La contraseña debe contener al menos un número", 
                "PASSWORD_NO_DIGIT"
            );
        }
        
        if (!hasSpecialChar(password)) {
            throw new BusinessException(
                "La contraseña debe contener al menos un carácter especial", 
                "PASSWORD_NO_SPECIAL_CHAR"
            );
        }
    }
    
    /**
     * Valida nombres y apellidos
     */
    public void validateNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new BusinessException("Los nombres son obligatorios", "NOMBRES_REQUIRED");
        }
        
        if (nombres.length() > 255) {
            throw new BusinessException(
                "Los nombres no pueden exceder los 255 caracteres", 
                "NOMBRES_TOO_LONG"
            );
        }
    }
    
    public void validateApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new BusinessException("Los apellidos son obligatorios", "APELLIDOS_REQUIRED");
        }
        
        if (apellidos.length() > 255) {
            throw new BusinessException(
                "Los apellidos no pueden exceder los 255 caracteres", 
                "APELLIDOS_TOO_LONG"
            );
        }
    }
    
    // Métodos auxiliares para validación de contraseña
    private boolean hasUpperCase(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }
    
    private boolean hasLowerCase(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }
    
    private boolean hasDigit(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }
    
    private boolean hasSpecialChar(String password) {
        return password.chars().anyMatch(ch -> "!@#$%^&*(),.?\":{}|<>".indexOf(ch) >= 0);
    }
}