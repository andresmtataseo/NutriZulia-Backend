package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponseDto;
import com.nutrizulia.dto.auth.SignUpRequestDto;
import com.nutrizulia.dto.auth.SignInRequestDto;
import com.nutrizulia.jwt.JwtService;

import com.nutrizulia.mapper.UsuarioMapper;
import com.nutrizulia.model.admin.Usuario;
import com.nutrizulia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper userMapper;

    public AuthResponseDto signIn(SignInRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCedula(), request.getClave()));
        UserDetails user = userRepository.findByCedulaWithRoles(request.getCedula()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .user(userMapper.toUserResponseDto(userRepository.findByCedula(request.getCedula()).orElseThrow()))
                .build();
    }


    public AuthResponseDto signUp(SignUpRequestDto request) {
        Usuario user = userMapper.toUser(request);
        user.setClave(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(true);
        user = userRepository.save(user);

        return AuthResponseDto.builder()
                .token(jwtService.getToken(user))
                .type("Bearer")
                .user(userMapper.toUserResponseDto(user))
                .build();
    }
}