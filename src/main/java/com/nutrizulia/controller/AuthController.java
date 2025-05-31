package com.nutrizulia.controller;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.dto.error.ErrorResponse;
import com.nutrizulia.service.IAuthService;
import com.nutrizulia.dto.auth.LoginRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Autenticación", description = "API para la autenticación de usuario")
public class AuthController {

    private final IAuthService authService;

    @Operation(
            summary = "Iniciar sesión con cédula y contraseña",
            description = "Permite a los usuarios iniciar sesión en el sistema utilizando su número de cédula y contraseña. Si las credenciales son válidas, se retorna un token de autenticación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

//    @Operation(summary = "Registrar un nuevo usuario")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Registro exitoso",
//                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
//            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe",
//                    content = @Content)
//    })
//    @PostMapping(value = "register")
//    public ResponseEntity<AuthResponse> register(
//            @Valid @RequestBody RegisterRequest request
//    ) {
//        return ResponseEntity.ok(authService.register(request));
//    }
