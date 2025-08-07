package com.nutrizulia.features.auth.controller;

import com.nutrizulia.features.auth.dto.*;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.auth.service.IAuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE_URL)
@RequiredArgsConstructor
@Tag(
        name = "Autenticación y Seguridad",
        description = "Manejo de inicio de sesión, registro de usuarios y emisión de tokens JWT."
)
public class AuthController {

    private final IAuthService authService;

    @Operation(
            summary = "Inicia sesión de un usuario",
            description = "Autentica a un usuario con su cédula y contraseña y devuelve un token JWT con los datos del usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (ej. formato de cédula, campos vacíos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas (cédula o contraseña incorrectos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.AUTH_SIGN_IN)
    @SecurityRequirements({})
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto){
        ApiResponseDto<AuthResponseDto> response = authService.signIn(signInRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(
            summary = "Verifica el estado de autenticación",
            description = "Endpoint protegido que confirma si el usuario está autenticado y si el token JWT aún no ha expirado. Retorna información del usuario autenticado.",
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
    @GetMapping(ApiConstants.AUTH_CHECK)
    public ResponseEntity<ApiResponseDto<Object>> checkAuth(Authentication authentication){
        // Obtener la cédula del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cedula = userDetails.getUsername();
        
        // Verificar el estado de autenticación
        ApiResponseDto<Object> response = authService.checkAuthStatus(cedula);
        return ResponseEntity.status(response.getStatus()).body(response);
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
    @PostMapping(ApiConstants.AUTH_FORGOT_PASSWORD)
    @SecurityRequirements({})
    public ResponseEntity<ApiResponseDto<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        ApiResponseDto<Object> response = authService.forgotPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite al usuario autenticado cambiar su contraseña actual por una nueva. Requiere autenticación JWT.",
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
    @PostMapping(ApiConstants.AUTH_CHANGE_PASSWORD)
    public ResponseEntity<ApiResponseDto<Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto request,
            Authentication authentication) {
        
        // Obtener la cédula del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cedula = userDetails.getUsername();
        
        // Cambiar la contraseña
        ApiResponseDto<Object> response = authService.changePassword(request, cedula);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el token JWT actual del usuario, cerrando su sesión de forma segura. El token se agrega a una blacklist para prevenir su uso posterior.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sesión cerrada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado (token ausente, inválido o expirado)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.AUTH_LOGOUT)
    public ResponseEntity<ApiResponseDto<Object>> logout(
            HttpServletRequest request,
            Authentication authentication) {
        
        // Extraer token del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponseDto.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Token de autorización requerido")
                    .build()
            );
        }
        
        String token = authHeader.substring(7); // Remover "Bearer "
        
        // Obtener cédula del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cedula = userDetails.getUsername();
        
        // Procesar logout
        ApiResponseDto<Object> response = authService.logout(token, cedula);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(
            summary = "Inicia sesión de un usuario web",
            description = "Autentica a un usuario web con su cédula y contraseña y devuelve un token JWT con los datos del usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (ej. formato de cédula, campos vacíos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas (cédula o contraseña incorrectos)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @PostMapping(ApiConstants.AUTH_SIGN_IN_ADMIN)
    @SecurityRequirements({})
    public ResponseEntity<ApiResponseDto<AuthAdminResponseDto>> signInAdmin(@Valid @RequestBody SignInRequestDto signInRequestDto){
        ApiResponseDto<AuthAdminResponseDto> response = authService.signInAdmin(signInRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}