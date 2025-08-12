package com.nutrizulia.common.exception;

/**
 * Excepción personalizada para recursos duplicados
 */
public class DuplicateResourceException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(createUserFriendlyMessage(resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public DuplicateResourceException(String message) {
        super(message);
        this.resourceName = "Recurso";
        this.fieldName = "campo";
        this.fieldValue = null;
    }
    
    private static String createUserFriendlyMessage(String resourceName, String fieldName, Object fieldValue) {
        // Mapeo de nombres técnicos a nombres amigables
        String friendlyResourceName = getFriendlyResourceName(resourceName);
        String friendlyFieldName = getFriendlyFieldName(fieldName);
        
        if (fieldValue != null) {
            return String.format("Ya existe %s con %s '%s'. Por favor, utilice un valor diferente.", 
                    friendlyResourceName, friendlyFieldName, fieldValue);
        } else {
            return String.format("Ya existe %s con estos datos. Por favor, verifique la información ingresada.", 
                    friendlyResourceName);
        }
    }
    
    private static String getFriendlyResourceName(String resourceName) {
        return switch (resourceName.toLowerCase()) {
            case "usuario" -> "un usuario";
            case "usuarioinstitucion" -> "una asignación de usuario";
            case "institucion" -> "una institución";
            case "rol" -> "un rol";
            default -> "un registro";
        };
    }
    
    private static String getFriendlyFieldName(String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "id" -> "identificador";
            case "cedula" -> "cédula";
            case "correo", "email" -> "correo electrónico";
            case "telefono", "phone" -> "número de teléfono";
            case "nombre", "name" -> "nombre";
            case "usuario_id, institucion_id" -> "esta combinación de usuario e institución";
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