package com.nutrizulia.auth.service;

import com.nutrizulia.auth.dto.AuthResponseDto;
import com.nutrizulia.auth.dto.SignUpRequestDto;
import com.nutrizulia.auth.dto.SignInRequestDto;
import com.nutrizulia.auth.jwt.JwtService;

import com.nutrizulia.user.mapper.UsuarioMapper;
import com.nutrizulia.user.model.Usuario;
import com.nutrizulia.user.repository.UsuarioRepository;
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