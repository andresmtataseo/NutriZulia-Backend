package com.nutrizulia.common.util;

import java.security.SecureRandom;

/**
 * Utilidad para operaciones relacionadas con contraseñas
 */
public class PasswordUtils {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    
    private static final SecureRandom random = new SecureRandom();
    private static final int DEFAULT_PASSWORD_LENGTH = 12;

    /**
     * Genera una contraseña aleatoria segura con longitud por defecto
     * @return contraseña aleatoria
     */
    public static String generateRandomPassword() {
        return generateRandomPassword(DEFAULT_PASSWORD_LENGTH);
    }

    /**
     * Genera una contraseña aleatoria segura con la longitud especificada
     * @param length longitud de la contraseña
     * @return contraseña aleatoria
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("La longitud de la contraseña debe ser al menos 8 caracteres");
        }

        StringBuilder password = new StringBuilder(length);
        
        // Asegurar que la contraseña tenga al menos un carácter de cada tipo
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        
        // Completar el resto de la contraseña con caracteres aleatorios
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        
        // Mezclar los caracteres para que no sigan un patrón predecible
        return shuffleString(password.toString());
    }

    /**
     * Mezcla los caracteres de una cadena
     * @param input cadena a mezclar
     * @return cadena mezclada
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}