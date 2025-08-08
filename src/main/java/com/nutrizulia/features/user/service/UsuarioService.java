package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.common.enums.Genero;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.mapper.UsuarioConInstitucionesMapper;
import com.nutrizulia.features.user.mapper.UsuarioMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Usuario findByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con cédula " + cedula + " no encontrado"));
    }

    @Override
    public UsuarioDto saveUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        usuario.setClave(passwordEncoder.encode(usuarioDto.getClave()));
        usuario.setIsEnabled(true);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Override
    public UsuarioDto createUsuario(UsuarioDto usuarioDto) {
        // Validaciones de negocio
        validateUsuarioData(usuarioDto);
        
        // Verificar si ya existe un usuario con la misma cédula
        if (usuarioRepository.findByCedula(usuarioDto.getCedula()).isPresent()) {
            throw new DataIntegrityViolationException("Ya existe un usuario con la cédula: " + usuarioDto.getCedula());
        }
        
        // Verificar si ya existe un usuario con el mismo correo
        if (usuarioRepository.findByCorreo(usuarioDto.getCorreo()).isPresent()) {
            throw new DataIntegrityViolationException("Ya existe un usuario con el correo: " + usuarioDto.getCorreo());
        }
        
        // Verificar si ya existe un usuario con el mismo teléfono
        if (usuarioRepository.findByTelefono(usuarioDto.getTelefono()).isPresent()) {
            throw new DataIntegrityViolationException("Ya existe un usuario con el teléfono: " + usuarioDto.getTelefono());
        }
        
        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        
        // Encriptar contraseña
        usuario.setClave(passwordEncoder.encode(usuarioDto.getClave()));
        
        // Establecer valores por defecto
        usuario.setIsEnabled(usuarioDto.getIs_enabled() != null ? usuarioDto.getIs_enabled() : true);
        
        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Convertir a DTO y retornar
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Override
    public void updatePassword(String cedula, String newPassword) {
        Usuario usuario = findByCedula(cedula);
        usuario.setClave(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    @Override
    public boolean isCedulaAvailable(String cedula) {
        return usuarioRepository.findByCedula(cedula).isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return usuarioRepository.findByCorreo(email).isEmpty();
    }

    @Override
    public boolean isPhoneAvailable(String phone) {
        return usuarioRepository.findByTelefono(phone).isEmpty();
    }

    private void validateUsuarioData(UsuarioDto usuarioDto) {
        // Validar género
        if (usuarioDto.getGenero() != null) {
            try {
                Genero.valueOf(usuarioDto.getGenero().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Género inválido. Los valores permitidos son: MASCULINO, FEMENINO");
            }
        }
        
        // Validar formato de cédula (debe empezar con V- o E-)
        if (usuarioDto.getCedula() != null && !usuarioDto.getCedula().matches("^[VE]-\\d{7,8}$")) {
            throw new IllegalArgumentException("Formato de cédula inválido. Debe ser V-12345678 o E-12345678");
        }
        
        // Validar formato de teléfono (debe ser 0XXX-XXXXXXX)
        if (usuarioDto.getTelefono() != null && !usuarioDto.getTelefono().matches("^0\\d{3}-\\d{7}$")) {
            throw new IllegalArgumentException("Formato de teléfono inválido. Debe ser 0XXX-XXXXXXX");
        }
        
        // Validar edad mínima (18 años)
        if (usuarioDto.getFechaNacimiento() != null) {
            java.time.LocalDate fechaNacimiento = usuarioDto.getFechaNacimiento();
            java.time.LocalDate fechaMinima = java.time.LocalDate.now().minusYears(18);
            if (fechaNacimiento.isAfter(fechaMinima)) {
                throw new IllegalArgumentException("El usuario debe ser mayor de 18 años");
            }
            
            // Validar que no sea mayor de 100 años
            java.time.LocalDate fechaMaxima = java.time.LocalDate.now().minusYears(150);
            if (fechaNacimiento.isBefore(fechaMaxima)) {
                throw new IllegalArgumentException("Fecha de nacimiento inválida");
            }
        }
    }

    @Override
    public PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(
            int page, int size, String search, String sortBy, String sortDir) {
        
        // Validar parámetros
        page = Math.max(0, page);
        size = Math.min(Math.max(1, size), 100); // Máximo 100 registros por página
        
        // Validar y mapear campo de ordenamiento
        String validSortBy = validateAndMapSortField(sortBy);
        
        // Validar dirección de ordenamiento
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "ASC";
        }
        
        // Crear Sort
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), validSortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Ejecutar consulta
        Page<Usuario> usuariosPage;
        if (search != null && !search.trim().isEmpty()) {
            usuariosPage = usuarioRepository.findUsuariosWithInstitucionesBySearch(search.trim(), pageable);
        } else {
            usuariosPage = usuarioRepository.findAllUsuariosWithInstituciones(pageable);
        }
        
        // Cargar las relaciones manualmente para los usuarios obtenidos
        List<Usuario> usuariosConRelaciones = usuariosPage.getContent().stream()
                .map(usuario -> usuarioRepository.findByCedulaWithRoles(usuario.getCedula()).orElse(usuario))
                .collect(Collectors.toList());
        
        // Crear nueva página con las relaciones cargadas
        Page<Usuario> usuariosPageConRelaciones = new PageImpl<>(
                usuariosConRelaciones, 
                pageable, 
                usuariosPage.getTotalElements()
        );
        
        // Mapear a DTO
        return usuarioConInstitucionesMapper.toPageDto(usuariosPageConRelaciones);
    }
    
    /**
     * Valida y mapea los campos de ordenamiento del frontend a los campos de la entidad
     */
    private String validateAndMapSortField(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "nombres";
        }
        
        // Mapear campos del frontend a campos de la entidad
        return switch (sortBy.toLowerCase()) {
            case "nombres" -> "nombres";
            case "apellidos" -> "apellidos";
            case "cedula" -> "cedula";
            case "correo" -> "correo";
            case "telefono" -> "telefono";
            case "fechanacimiento" -> "fechaNacimiento";
            default -> "nombres"; // Campo por defecto
        };
    }

}
