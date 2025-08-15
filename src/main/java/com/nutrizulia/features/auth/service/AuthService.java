package com.nutrizulia.features.auth.service;

import com.nutrizulia.common.service.EmailService;
import com.nutrizulia.common.util.PasswordUtils;
import com.nutrizulia.features.auth.dto.*;
import com.nutrizulia.features.auth.jwt.JwtService;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.user.mapper.UsuarioMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import com.nutrizulia.features.user.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.DisabledException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;


    public ApiResponseDto<AuthResponseDto> signIn(SignInRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave())
            );
            Usuario user = usuarioService.findByCedula(request.getCedula());
            
            // Verificar que el usuario esté activo en al menos una institución
            List<UsuarioInstitucion> institucionesActivas = usuarioInstitucionRepository.findActiveInstitutionsByUserId(user.getId());
            if (institucionesActivas.isEmpty()) {
                throw new DisabledException("El usuario no está activo en ninguna institución");
            }

            String token = jwtService.generateToken(user);

            AuthResponseDto authData = AuthResponseDto.builder()
                    .token(token)
                    .type("Bearer")
                    .user(userMapper.toDto(user))
                    .build();

            return ApiResponseDto.<AuthResponseDto>builder()
                    .status(HttpStatus.OK.value())
                    .message("Inicio de sesión exitoso")
                    .data(authData)
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_SIGN_IN)
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas. Verifique su cédula y contraseña.");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Credenciales inválidas. Verifique su cédula y contraseña.");
        } catch (DisabledException e) {
            throw e; // Propaga para que el GlobalExceptionHandler retorne una respuesta amigable y estandarizada
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al procesar el inicio de sesión");
        }
    }

    @Transactional
    public ApiResponseDto<Object> forgotPassword(ForgotPasswordRequestDto request) {
        try {
            // Buscar el usuario por cédula
            Usuario user = usuarioService.findByCedula(request.getCedula());

            // Generar nueva contraseña temporal usando PasswordUtils
            String nuevaClaveTemp = PasswordUtils.generateRandomPassword();
            
            // Actualizar la contraseña en la base de datos
            // El método updatePassword ya se encarga de la encriptación
            usuarioService.updatePassword(user.getId(), nuevaClaveTemp);
            
            emailService.recuperacionClave(user.getCorreo(), user.getNombres() + " " + user.getApellidos(), nuevaClaveTemp);
            
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Si la cédula existe en nuestro sistema, se ha enviado una nueva contraseña temporal a su correo electrónico.")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_FORGOT_PASSWORD)
                    .build();
                    
        } catch (UsernameNotFoundException e) {
            // Por seguridad, devolvemos el mismo mensaje aunque el usuario no exista
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Si la cédula existe en nuestro sistema, se ha enviado una nueva contraseña temporal a su correo electrónico.")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_FORGOT_PASSWORD)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al procesar la solicitud de recuperación de contraseña");
        }
    }

    @Transactional
    public ApiResponseDto<Object> changePassword(ChangePasswordRequestDto request, String cedula) {
        try {
            // Buscar el usuario por cédula
            Usuario user = usuarioService.findByCedula(cedula);

            // Verificar que la contraseña actual sea correcta
            if (!passwordEncoder.matches(request.getClave_actual(), user.getClave())) {
                throw new IllegalArgumentException("La contraseña actual es incorrecta");
            }

            // Verificar que la nueva contraseña sea diferente a la actual
            if (passwordEncoder.matches(request.getClave_nueva(), user.getClave())) {
                throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
            }

            // Actualizar la contraseña en la base de datos
            // El método updatePassword ya se encarga de la validación y encriptación
            usuarioService.updatePassword(user.getId(), request.getClave_nueva());
            
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Contraseña cambiada exitosamente")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_CHANGE_PASSWORD)
                    .build();
            
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuario no encontrado");
        } catch (IllegalArgumentException e) {
            throw e; // Re-lanzar para que el GlobalExceptionHandler lo maneje
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al cambiar la contraseña");
        }
    }

    public ApiResponseDto<Object> checkAuthStatus(String cedula) {
        try {
            // Buscar el usuario por cédula para verificar que existe y está activo
            Usuario user = usuarioService.findByCedula(cedula);
            // Si llegamos aquí, significa que el token es válido y no ha expirado
            // (ya que pasó por el filtro JWT)
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Token válido y usuario autenticado correctamente")
                    .data(Map.of(
                            "cedula", user.getCedula(),
                            "nombres", user.getNombres(),
                            "apellidos", user.getApellidos(),
                            "correo", user.getCorreo(),
                            "authenticated", true,
                            "tokenValid", true
                    ))
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_CHECK)
                    .build();
                    
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuario no encontrado o token inválido");
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al verificar la autenticación");
        }
    }

    @Override
    public ApiResponseDto<Object> logout(String token, String cedula) {
        try {
            // Agregar el token a la blacklist
            jwtService.blacklistToken(token, cedula);
            
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Sesión cerrada exitosamente")
                    .data(Map.of(
                            "logoutTime", LocalDateTime.now(),
                            "message", "Token invalidado correctamente"
                    ))
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_LOGOUT)
                    .build();
                    
        } catch (Exception e) {
            // Incluso si hay error al agregar a blacklist, consideramos el logout exitoso
            // ya que el token expirará naturalmente
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Sesión cerrada exitosamente")
                    .data(Map.of(
                            "logoutTime", LocalDateTime.now(),
                            "message", "Logout procesado (token expirará naturalmente)"
                    ))
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_LOGOUT)
                    .build();
        }
    }
}