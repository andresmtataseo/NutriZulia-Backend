package com.nutrizulia.common.service;

import com.nutrizulia.common.exception.DuplicateResourceException;
import com.nutrizulia.common.validator.UserValidator;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio centralizado para verificar la disponibilidad de datos únicos
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DataAvailabilityService {

    private final UsuarioRepository usuarioRepository;
    private final UserValidator userValidator;

    /**
     * Verifica si una cédula está disponible
     */
    public boolean isCedulaAvailable(String cedula) {
        log.debug("Verificando disponibilidad de cédula: {}", cedula);
        userValidator.validateCedula(cedula);
        return usuarioRepository.findByCedula(cedula).isEmpty();
    }

    /**
     * Verifica si un correo está disponible
     */
    public boolean isEmailAvailable(String email) {
        log.debug("Verificando disponibilidad de correo: {}", email);
        userValidator.validateEmail(email);
        return usuarioRepository.findByCorreo(email).isEmpty();
    }

    /**
     * Verifica si un teléfono está disponible
     */
    public boolean isPhoneAvailable(String phone) {
        log.debug("Verificando disponibilidad de teléfono: {}", phone);
        if (phone != null && !phone.trim().isEmpty()) {
            userValidator.validateTelefono(phone);
        }
        return usuarioRepository.findByTelefono(phone).isEmpty();
    }

    /**
     * Verifica la disponibilidad de cédula y lanza excepción si no está disponible
     */
    public void checkCedulaAvailability(String cedula) {
        if (!isCedulaAvailable(cedula)) {
            throw new DuplicateResourceException("Usuario", "cédula", cedula);
        }
    }

    /**
     * Verifica la disponibilidad de correo y lanza excepción si no está disponible
     */
    public void checkEmailAvailability(String email) {
        if (!isEmailAvailable(email)) {
            throw new DuplicateResourceException("Usuario", "correo", email);
        }
    }

    /**
     * Verifica la disponibilidad de teléfono y lanza excepción si no está disponible
     */
    public void checkPhoneAvailability(String phone) {
        if (phone != null && !phone.trim().isEmpty() && !isPhoneAvailable(phone)) {
            throw new DuplicateResourceException("Usuario", "teléfono", phone);
        }
    }

    /**
     * Verifica la disponibilidad de todos los datos únicos de un usuario
     */
    public void checkUserDataAvailability(String cedula, String email, String phone) {
        log.debug("Verificando disponibilidad de datos únicos - cédula: {}, correo: {}, teléfono: {}", 
                 cedula, email, phone);
        
        checkCedulaAvailability(cedula);
        checkEmailAvailability(email);
        checkPhoneAvailability(phone);
    }
}