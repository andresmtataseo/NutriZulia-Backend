package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.dto.UsuarioInstitucionDto;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;
import com.nutrizulia.jwt.JwtService;
import com.nutrizulia.model.Usuario;
import com.nutrizulia.model.UsuarioInstitucion;
import com.nutrizulia.repository.UsuarioInstitucionRepository;

import com.nutrizulia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioInstitucionRepository usuarioInstitucionRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave()));
        Usuario usuario = usuarioRepository.findByCedula(request.getCedula()).orElseThrow();
        String token = jwtService.getToken((UserDetails) usuario);

        List<UsuarioIn stitucion> rolesPorInstitucion = usuarioInstitucionRepository.findByUsuario(usuario);

        return AuthResponse.builder()
                .token(token)
                .usuario(usuario)
                .rolesPorInstitucion(rolesPorInstitucion)
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
