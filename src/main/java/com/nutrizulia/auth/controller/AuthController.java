package com.nutrizulia.auth.controller;

import com.nutrizulia.auth.dto.AuthResponseDto;
import com.nutrizulia.auth.dto.SignUpRequestDto;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.auth.service.AuthService;
import com.nutrizulia.auth.dto.SignInRequestDto;
import com.nutrizulia.common.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_API_BASE_URL)
@RequiredArgsConstructor
@Tag(
        name = "Autenticación y Seguridad",
        description = "Manejo de inicio de sesión, registro de usuarios y emisión de tokens JWT."
)
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<ApiResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(authService.signUp(signUpRequestDto));
    }

    @Operation(
            summary = "Verifica el estado de autenticación",
            description = "Endpoint protegido que confirma si el usuario está autenticado. Requiere un token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticado correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado (token ausente o inválido)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    @GetMapping(ApiConstants.CHECK_AUTH_URL)
    public String checkAuth(){
        return "Hola mundo";
    }
}