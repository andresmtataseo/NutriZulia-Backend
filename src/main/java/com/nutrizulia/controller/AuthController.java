package com.nutrizulia.controller;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.service.IAuthService;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Operation(summary = "Iniciar sesión con correo y contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content)
    })
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
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
}
