package com.nutrizulia.features.auth.validation;

import com.nutrizulia.features.auth.dto.ChangePasswordRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordRequestDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // No se requiere inicialización específica
    }

    @Override
    public boolean isValid(ChangePasswordRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true; // Dejar que @NotNull maneje los valores nulos
        }

        if (dto.getClave_nueva() == null || dto.getClave_nueva_confirmacion() == null) {
            return true; // Dejar que @NotBlank maneje los valores nulos
        }

        boolean isValid = dto.getClave_nueva().equals(dto.getClave_nueva_confirmacion());

        if (!isValid) {
            // Personalizar el mensaje de error
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La nueva contraseña y su confirmación no coinciden")
                    .addPropertyNode("claveNuevaConfirmacion")
                    .addConstraintViolation();
        }

        return isValid;
    }
}