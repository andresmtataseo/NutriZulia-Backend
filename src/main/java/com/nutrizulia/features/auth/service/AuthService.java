package com.nutrizulia.features.auth.service;

import com.nutrizulia.common.service.EmailService;
import com.nutrizulia.features.auth.dto.AuthResponseDto;
import com.nutrizulia.features.auth.dto.ChangePasswordRequestDto;
import com.nutrizulia.features.auth.dto.ForgotPasswordRequestDto;
import com.nutrizulia.features.auth.dto.SignUpRequestDto;
import com.nutrizulia.features.auth.dto.SignInRequestDto;
import com.nutrizulia.features.auth.jwt.JwtService;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.user.mapper.UsuarioMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
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


    public ApiResponseDto<AuthResponseDto> signIn(SignInRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave())
        );
        Usuario user = usuarioService.findByCedulaWithRoles(request.getCedula())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la cédula: " + request.getCedula()));

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
                .path(ApiConstants.AUTH_API_BASE_URL + ApiConstants.SIGN_IN_URL)
                .build();
    }

    public ApiResponseDto<Object> signUp(SignUpRequestDto request) {
        usuarioService.save(request);
        return ApiResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("Usuario registrado exitosamente")
                .timestamp(LocalDateTime.now())
                .path(ApiConstants.SIGN_UP_URL)
                .build();
    }

    @Transactional
    public ApiResponseDto<Object> forgotPassword(ForgotPasswordRequestDto request) {
        try {
            // Buscar el usuario por cédula
            Usuario user = usuarioService.findByCedulaWithRoles(request.getCedula())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la cédula: " + request.getCedula()));

            // Generar nueva contraseña temporal
            String nuevaClaveTemp = generateTemporaryPassword();
            
            // Codificar la nueva contraseña
            String claveEncriptada = passwordEncoder.encode(nuevaClaveTemp);
            
            // Actualizar la contraseña en la base de datos
            user.setClave(claveEncriptada);
            usuarioService.updatePassword(user.getId(), claveEncriptada);
            
            emailService.recuperacionClave(user.getCorreo(), user.getNombres() + " " + user.getApellidos(), nuevaClaveTemp);
            
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Si la cédula existe en nuestro sistema, se ha enviado una nueva contraseña temporal a su correo electrónico.")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_API_BASE_URL + ApiConstants.FORGOT_PASSWORD_URL)
                    .build();
                    
        } catch (UsernameNotFoundException e) {
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Si la cédula existe en nuestro sistema, se ha enviado una nueva contraseña temporal a su correo electrónico.")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_API_BASE_URL + ApiConstants.FORGOT_PASSWORD_URL)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al procesar la solicitud de recuperación de contraseña");
        }
    }

    @Transactional
    public ApiResponseDto<Object> changePassword(ChangePasswordRequestDto request, String cedula) {
        try {
            // Buscar el usuario por cédula
            Usuario user = usuarioService.findByCedulaWithRoles(cedula)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la cédula: " + cedula));

            // Verificar que la contraseña actual sea correcta
            if (!passwordEncoder.matches(request.getClave_actual(), user.getClave())) {
                throw new IllegalArgumentException("La contraseña actual es incorrecta");
            }

            // Verificar que la nueva contraseña sea diferente a la actual
            if (passwordEncoder.matches(request.getClave_nueva(), user.getClave())) {
                throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
            }

            // Codificar la nueva contraseña
            String claveEncriptada = passwordEncoder.encode(request.getClave_nueva());
            
            // Actualizar la contraseña en la base de datos
            usuarioService.updatePassword(user.getId(), claveEncriptada);
            
            return ApiResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("Contraseña cambiada exitosamente")
                    .timestamp(LocalDateTime.now())
                    .path(ApiConstants.AUTH_API_BASE_URL + ApiConstants.CHANGE_PASSWORD_URL)
                    .build();
            
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al cambiar la contraseña");
        }
    }

    public ApiResponseDto<Object> checkAuthStatus(String cedula) {
        try {
            // Buscar el usuario por cédula para verificar que existe y está activo
            Usuario user = usuarioService.findByCedulaWithRoles(cedula)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la cédula: " + cedula));

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
                    .path(ApiConstants.AUTH_API_BASE_URL + ApiConstants.CHECK_AUTH_URL)
                    .build();
                    
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno del servidor al verificar la autenticación");
        }
    }

    private String generateTemporaryPassword() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        // Generar contraseña de 8 caracteres
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));
        }
        
        return password.toString();
    }
}