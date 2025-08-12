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
            throw new BusinessException("Por favor, ingrese su número de cédula", "CEDULA_REQUIRED");
        }
        
        if (!CEDULA_PATTERN.matcher(cedula).matches()) {
            throw new BusinessException(
                "El formato de la cédula no es válido. Debe incluir la nacionalidad seguida de un guión y el número (ejemplo: V-12345678 o E-12345678)", 
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
                    "El formato del teléfono no es válido. Debe incluir el prefijo seguido de un guión y 7 dígitos (ejemplo: 0424-1234567)", 
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
            throw new BusinessException("Por favor, ingrese su dirección de correo electrónico", "EMAIL_REQUIRED");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(
                "El formato del correo electrónico no es válido. Asegúrese de incluir el símbolo @ y un dominio válido", 
                "EMAIL_INVALID_FORMAT"
            );
        }
    }
    
    /**
     * Valida el género
     */
    public void validateGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new BusinessException("Por favor, seleccione el género", "GENERO_REQUIRED");
        }
        
        try {
            Genero.valueOf(genero.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(
                "El género seleccionado no es válido. Por favor, seleccione Masculino o Femenino", 
                "GENERO_INVALID"
            );
        }
    }
    
    /**
     * Valida la fecha de nacimiento
     */
    public void validateFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new BusinessException("Por favor, ingrese la fecha de nacimiento", "FECHA_NACIMIENTO_REQUIRED");
        }
        
        LocalDate fechaMinima = LocalDate.now().minusYears(MIN_AGE);
        if (fechaNacimiento.isAfter(fechaMinima)) {
            throw new BusinessException(
                String.format("El usuario debe ser mayor de %d años para poder registrarse en el sistema", MIN_AGE), 
                "EDAD_MINIMA_NO_CUMPLIDA"
            );
        }
        
        LocalDate fechaMaxima = LocalDate.now().minusYears(MAX_AGE);
        if (fechaNacimiento.isBefore(fechaMaxima)) {
            throw new BusinessException(
                "La fecha de nacimiento ingresada no es válida. Por favor, verifique la información", 
                "FECHA_NACIMIENTO_INVALIDA"
            );
        }
    }
    
    /**
     * Valida la contraseña
     */
    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("Por favor, ingrese una contraseña", "PASSWORD_REQUIRED");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BusinessException(
                String.format("La contraseña debe tener al menos %d caracteres para garantizar la seguridad", MIN_PASSWORD_LENGTH), 
                "PASSWORD_TOO_SHORT"
            );
        }
        
        // Validaciones adicionales de complejidad de contraseña
        if (!hasUpperCase(password)) {
            throw new BusinessException(
                "La contraseña debe incluir al menos una letra mayúscula para mayor seguridad", 
                "PASSWORD_NO_UPPERCASE"
            );
        }
        
        if (!hasLowerCase(password)) {
            throw new BusinessException(
                "La contraseña debe incluir al menos una letra minúscula para mayor seguridad", 
                "PASSWORD_NO_LOWERCASE"
            );
        }
        
        if (!hasDigit(password)) {
            throw new BusinessException(
                "La contraseña debe incluir al menos un número para mayor seguridad", 
                "PASSWORD_NO_DIGIT"
            );
        }
        
        if (!hasSpecialChar(password)) {
            throw new BusinessException(
                "La contraseña debe incluir al menos un carácter especial (!@#$%^&*(),.?\":{}|<>) para mayor seguridad", 
                "PASSWORD_NO_SPECIAL_CHAR"
            );
        }
    }
    
    /**
     * Valida nombres y apellidos
     */
    public void validateNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new BusinessException("Por favor, ingrese los nombres del usuario", "NOMBRES_REQUIRED");
        }
        
        if (nombres.length() > 255) {
            throw new BusinessException(
                "Los nombres son demasiado largos. Por favor, use un máximo de 255 caracteres", 
                "NOMBRES_TOO_LONG"
            );
        }
    }
    
    public void validateApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new BusinessException("Por favor, ingrese los apellidos del usuario", "APELLIDOS_REQUIRED");
        }
        
        if (apellidos.length() > 255) {
            throw new BusinessException(
                "Los apellidos son demasiado largos. Por favor, use un máximo de 255 caracteres", 
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