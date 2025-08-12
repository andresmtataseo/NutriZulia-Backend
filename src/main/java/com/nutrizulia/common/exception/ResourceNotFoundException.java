package com.nutrizulia.common.exception;

/**
 * Excepción personalizada para recursos no encontrados
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(createUserFriendlyMessage(resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = "Recurso";
        this.fieldName = "id";
        this.fieldValue = null;
    }
    
    private static String createUserFriendlyMessage(String resourceName, String fieldName, Object fieldValue) {
        // Mapeo de nombres técnicos a nombres amigables
        String friendlyResourceName = getFriendlyResourceName(resourceName);
        String friendlyFieldName = getFriendlyFieldName(fieldName);
        
        return String.format("No se encontró %s con %s '%s'. Por favor, verifique que la información sea correcta.", 
                friendlyResourceName, friendlyFieldName, fieldValue);
    }
    
    private static String getFriendlyResourceName(String resourceName) {
        return switch (resourceName.toLowerCase()) {
            case "usuario" -> "el usuario";
            case "usuarioinstitucion" -> "la asignación de usuario";
            case "institucion" -> "la institución";
            case "rol" -> "el rol";
            default -> "el recurso solicitado";
        };
    }
    
    private static String getFriendlyFieldName(String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "id" -> "identificador";
            case "cedula" -> "cédula";
            case "correo", "email" -> "correo electrónico";
            case "telefono", "phone" -> "número de teléfono";
            case "nombre", "name" -> "nombre";
            default -> fieldName;
        };
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getFieldValue() {
        return fieldValue;
    }
}