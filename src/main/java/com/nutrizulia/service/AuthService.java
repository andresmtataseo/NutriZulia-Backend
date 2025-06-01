package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.dto.auth.UsuarioInstitucionDto;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;
import com.nutrizulia.jwt.JwtService;
import com.nutrizulia.model.Usuario;
import com.nutrizulia.model.UsuarioInstitucion;
import com.nutrizulia.repository.UsuarioInstitucionRepository;

import com.nutrizulia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave()));
        Usuario usuario = usuarioRepository.findByCedula(request.getCedula()).orElseThrow();
        String token = jwtService.getToken(usuario);

        List<UsuarioInstitucion> rolesPorInstitucion = usuarioInstitucionRepository.findByUsuario(usuario);

        // 5. Mapear la lista de entidades UsuarioInstitucion a DTOs
        List<UsuarioInstitucionDto> institucionesRolesDto = rolesPorInstitucion.stream()
                .map(ui -> UsuarioInstitucionDto.builder()
                        .id(ui.getId())
                        .idUsuario(ui.getUsuario().getId())
                        .institucionId(ui.getInstitucion().getId())
                        .institucionNombre(ui.getInstitucion().getNombre())
                        .rolId(ui.getRol().getId())
                        .rolNombre(ui.getRol().getNombre())
                        .build())
                .collect(Collectors.toList());

        // 6. Construir y devolver la respuesta de autenticación
        return AuthResponse.builder()
                .token(token)
                .id(usuario.getId())
                .cedula(usuario.getCedula())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .telefono(usuario.getTelefono())
                .correo(usuario.getCorreo())
                .institucionesRoles(institucionesRolesDto) // Asigna la lista de DTOs mapeados
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
