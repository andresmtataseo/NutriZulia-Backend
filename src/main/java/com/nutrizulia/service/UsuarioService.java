package com.nutrizulia.service;

import com.nutrizulia.dto.UsuarioDto;
import com.nutrizulia.mapper.UsuarioMapper;
import com.nutrizulia.model.Usuario;
import com.nutrizulia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<UsuarioDto> getUsuarios() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UsuarioDto getUsuarioByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula)
                .map(usuarioMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con cédula " + cedula + " no encontrado"));
    }

    @Override
    public Optional<Usuario> findByCedulaWithRoles(String cedula) {
        return usuarioRepository.findByCedulaWithRoles(cedula);
    }

}
