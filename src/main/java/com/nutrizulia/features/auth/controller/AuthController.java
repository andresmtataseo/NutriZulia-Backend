package com.nutrizulia.features.auth.controller;

import com.nutrizulia.features.auth.dto.AuthResponseDto;
import com.nutrizulia.features.auth.dto.ChangePasswordRequestDto;
import com.nutrizulia.features.auth.dto.ForgotPasswordRequestDto;
import com.nutrizulia.features.auth.dto.SignUpRequestDto;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.auth.dto.SignInRequestDto;
import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.auth.service.IAuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_API_BASE_URL)
@RequiredArgsConstructor
@Tag(
        name = "Autenticación y Seguridad",
        description = "Manejo de inicio de sesión, registro de usuarios y emisión de tokens JWT."
)
public class AuthController {

    private final IAuthService authService;

    @Operation(
            summary = "Inicia sesión de un usuario",
            description = "Autentica a un usuario con su email y contraseña y devuelve un token JWT con los datos del usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (ej. formato de email, campos vacíos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas (email o contraseña incorrectos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.SIGN_IN_URL)
    public ResponseEntity<AuthResponseDto> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto){
        return ResponseEntity.ok(authService.signIn(signInRequestDto));
    }

    @Operation(
            summary = "Registra un nuevo usuario",
            description = "Crea una nueva cuenta de usuario con el rol por defecto (CLIENTE) y devuelve un token JWT con los datos del usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email ya registrado o datos de registro inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.SIGN_UP_URL)
    public ResponseEntity<ApiResponseDto<Object>> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(authService.signUp(signUpRequestDto));
    }

    @Operation(
            summary = "Verifica el estado de autenticación",
            description = "Endpoint protegido que confirma si el usuario está autenticado y si el token JWT aún no ha expirado. Retorna información del usuario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token válido y usuario autenticado correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado (token ausente, inválido o expirado)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @GetMapping(ApiConstants.CHECK_AUTH_URL)
    public ResponseEntity<ApiResponseDto<Object>> checkAuth(Authentication authentication){
        // Obtener la cédula del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cedula = userDetails.getUsername();
        
        // Verificar el estado de autenticación
        ApiResponseDto<Object> response = authService.checkAuthStatus(cedula);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Recuperación de contraseña",
            description = "Genera una nueva contraseña temporal y la envía al correo electrónico del usuario asociado a la cédula proporcionada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud procesada exitosamente. Se ha enviado una nueva contraseña temporal al correo electrónico del usuario (si existe).",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (ej. formato de cédula, campos vacíos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.FORGOT_PASSWORD_URL)
    public ResponseEntity<ApiResponseDto<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        ApiResponseDto<Object> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite al usuario autenticado cambiar su contraseña actual por una nueva. Requiere autenticación JWT.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contraseña cambiada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (contraseñas no coinciden, formato inválido, etc.)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado (token ausente/inválido o contraseña actual incorrecta)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.CHANGE_PASSWORD_URL)
    public ResponseEntity<ApiResponseDto<Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto request,
            Authentication authentication) {
        
        // Obtener la cédula del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cedula = userDetails.getUsername();
        
        // Cambiar la contraseña
        ApiResponseDto<Object> response = authService.changePassword(request, cedula);
        return ResponseEntity.ok(response);
    }
}