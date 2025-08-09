package com.nutrizulia.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * Utilidad para sanitización de entrada y prevención de XSS
 */
@Component
public class InputSanitizer {
    
    // Patrones para detectar posibles ataques XSS
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
        "<script[^>]*>.*?</script>", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile(
        "javascript:", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern VBSCRIPT_PATTERN = Pattern.compile(
        "vbscript:", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern ONLOAD_PATTERN = Pattern.compile(
        "onload[^=]*=", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern ONERROR_PATTERN = Pattern.compile(
        "onerror[^=]*=", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern ONCLICK_PATTERN = Pattern.compile(
        "onclick[^=]*=", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern EVAL_PATTERN = Pattern.compile(
        "eval\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
        "expression\\s*\\(", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern[] XSS_PATTERNS = {
        SCRIPT_PATTERN,
        JAVASCRIPT_PATTERN,
        VBSCRIPT_PATTERN,
        ONLOAD_PATTERN,
        ONERROR_PATTERN,
        ONCLICK_PATTERN,
        EVAL_PATTERN,
        EXPRESSION_PATTERN
    };
    
    /**
     * Sanitiza una cadena de entrada para prevenir XSS
     * @param input La cadena a sanitizar
     * @return La cadena sanitizada
     */
    public String sanitize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        // Escapar caracteres HTML
        String sanitized = HtmlUtils.htmlEscape(input);
        
        // Remover patrones peligrosos
        for (Pattern pattern : XSS_PATTERNS) {
            sanitized = pattern.matcher(sanitized).replaceAll("");
        }
        
        return sanitized.trim();
    }
    
    /**
     * Sanitiza una cadena preservando algunos caracteres HTML básicos seguros
     * @param input La cadena a sanitizar
     * @return La cadena sanitizada
     */
    public String sanitizePreservingBasicHtml(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String sanitized = input;
        
        // Remover patrones peligrosos sin escapar HTML primero
        for (Pattern pattern : XSS_PATTERNS) {
            sanitized = pattern.matcher(sanitized).replaceAll("");
        }
        
        // Escapar solo caracteres peligrosos específicos
        sanitized = sanitized.replace("<script", "&lt;script")
                            .replace("</script>", "&lt;/script&gt;")
                            .replace("javascript:", "")
                            .replace("vbscript:", "")
                            .replace("onload=", "")
                            .replace("onerror=", "")
                            .replace("onclick=", "");
        
        return sanitized.trim();
    }
    
    /**
     * Valida si una cadena contiene posibles ataques XSS
     * @param input La cadena a validar
     * @return true si contiene patrones peligrosos, false en caso contrario
     */
    public boolean containsXssPatterns(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Sanitiza parámetros de búsqueda
     * @param searchTerm El término de búsqueda
     * @return El término sanitizado
     */
    public String sanitizeSearchTerm(String searchTerm) {
        if (searchTerm == null) {
            return null;
        }
        
        // Sanitizar y normalizar
        String sanitized = sanitize(searchTerm);
        
        // Remover caracteres especiales que podrían ser problemáticos en búsquedas
        sanitized = sanitized.replaceAll("[<>\"'%;()&+]", "");
        
        return sanitized.trim().isEmpty() ? null : sanitized;
    }
    
    /**
     * Sanitiza nombres y apellidos
     * @param name El nombre a sanitizar
     * @return El nombre sanitizado
     */
    public String sanitizeName(String name) {
        if (name == null) {
            return null;
        }
        
        // Permitir solo letras, espacios, acentos y algunos caracteres especiales comunes en nombres
        String sanitized = name.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]", "");
        
        return sanitized.trim();
    }
    
    /**
     * Sanitiza direcciones de correo electrónico
     * @param email El email a sanitizar
     * @return El email sanitizado
     */
    public String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        
        // Remover espacios y convertir a minúsculas
        String sanitized = email.trim().toLowerCase();
        
        // Permitir solo caracteres válidos para email
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9@._-]", "");
        
        return sanitized;
    }
    
    /**
     * Sanitiza números de teléfono
     * @param phone El teléfono a sanitizar
     * @return El teléfono sanitizado
     */
    public String sanitizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        
        // Permitir solo números, guiones y espacios
        String sanitized = phone.replaceAll("[^0-9\\s-]", "");
        
        return sanitized.trim();
    }
    
    /**
     * Sanitiza cédulas
     * @param cedula La cédula a sanitizar
     * @return La cédula sanitizada
     */
    public String sanitizeCedula(String cedula) {
        if (cedula == null) {
            return null;
        }
        
        // Permitir solo letras V/E, guiones y números
        String sanitized = cedula.toUpperCase().replaceAll("[^VE0-9-]", "");
        
        return sanitized.trim();
    }
}