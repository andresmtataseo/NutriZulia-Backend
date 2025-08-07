package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.auth.dto.SignUpRequestDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.mapper.UsuarioConInstitucionesMapper;
import com.nutrizulia.features.user.mapper.UsuarioMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioConInstitucionesMapper usuarioConInstitucionesMapper;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public Usuario save(SignUpRequestDto signUpRequestDto) {
        Usuario usuario = usuarioMapper.toEntity(signUpRequestDto);
        usuario.setClave(passwordEncoder.encode(usuario.getPassword()));
        usuario.setIsEnabled(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void updatePassword(Integer userId, String newEncodedPassword) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con ID " + userId + " no encontrado"));
        
        usuario.setClave(newEncodedPassword);
        usuarioRepository.save(usuario);
    }

    @Override
    public PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(
            int page, int size, String search, String sortBy, String sortDir) {
        
        // Validar parámetros
        page = Math.max(0, page);
        size = Math.min(Math.max(1, size), 100); // Máximo 100 registros por página
        
        // Validar campo de ordenamiento
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "nombres";
        }
        
        // Validar dirección de ordenamiento
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "ASC";
        }
        
        // Crear Sort
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Ejecutar consulta
        Page<Usuario> usuariosPage;
        if (search != null && !search.trim().isEmpty()) {
            usuariosPage = usuarioRepository.findUsuariosWithInstitucionesBySearch(search.trim(), pageable);
        } else {
            usuariosPage = usuarioRepository.findAllUsuariosWithInstituciones(pageable);
        }
        
        // Mapear a DTO
        return usuarioConInstitucionesMapper.toPageDto(usuariosPage);
    }

}
