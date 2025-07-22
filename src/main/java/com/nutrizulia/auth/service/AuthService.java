package com.nutrizulia.auth.service;

import com.nutrizulia.auth.dto.AuthResponseDto;
import com.nutrizulia.auth.dto.SignUpRequestDto;
import com.nutrizulia.auth.dto.SignInRequestDto;
import com.nutrizulia.auth.jwt.JwtService;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.user.mapper.UsuarioMapper;
import com.nutrizulia.user.model.Usuario;
import com.nutrizulia.user.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper userMapper;


    public AuthResponseDto signIn(SignInRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave())
        );
        Usuario user = usuarioService.findByCedulaWithRoles(request.getCedula())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la cédula: " + request.getCedula()));

        String token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .user(userMapper.toDto(user))
                .build();
    }

    public ApiResponseDto signUp(SignUpRequestDto request) {
        usuarioService.save(request);
        return ApiResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("Usuario registrado exitosamente")
                .timestamp(LocalDateTime.now())
                .path(ApiConstants.SIGN_UP_URL)
                .build();
    }
}