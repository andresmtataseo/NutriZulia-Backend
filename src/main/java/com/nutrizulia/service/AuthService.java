package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;
import com.nutrizulia.jwt.JwtService;
import com.nutrizulia.model.Usuario;

import com.nutrizulia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

    private final IUsuarioInstitucionService usuarioInstitucionService;
    private final IUsuarioService usuarioService;

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave()));

        Usuario usuario = usuarioRepository.findByCedula(request.getCedula()).orElseThrow();

        String token = jwtService.getToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .id(usuario.getId())
                .cedula(usuario.getCedula())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .telefono(usuario.getTelefono())
                .correo(usuario.getCorreo())
                .institucionesRoles(usuarioInstitucionService.getInstitucionesByUsuarioId(usuario.getId()))
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .cedula(request.getCedula())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .clave(passwordEncoder.encode(request.getClave()))
                .build();

        usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();

    }

}
