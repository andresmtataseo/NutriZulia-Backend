package com.nutrizulia.controller;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.service.IAuthService;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

}
