package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.common.exception.ResourceNotFoundException;
import com.nutrizulia.common.service.DataAvailabilityService;
import com.nutrizulia.common.util.ValidationUtils;
import com.nutrizulia.common.validator.UserValidator;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDetallesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.mapper.UsuarioConInstitucionesMapper;
import com.nutrizulia.features.user.mapper.UsuarioDetallesMapper;
import com.nutrizulia.features.user.mapper.UsuarioInstitucionMapper;
import com.nutrizulia.features.user.mapper.UsuarioMapper;
import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioInstitucionRepository usuarioInstitucionRepository;
    private final UsuarioConInstitucionesMapper usuarioConInstitucionesMapper;
    private final UsuarioDetallesMapper usuarioDetallesMapper;
    private final UsuarioInstitucionMapper usuarioInstitucionMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final DataAvailabilityService dataAvailabilityService;

    @Override
    public Usuario findByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "cedula", cedula));
    }

    @Override
    public Usuario findById(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
    }

    @Override
    @Transactional
    public UsuarioDto saveUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        usuario.setClave(passwordEncoder.encode(usuarioDto.getClave()));
        usuario.setIsEnabled(true);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Override
    @Transactional
    public UsuarioDto createUsuario(UsuarioDto usuarioDto) {
        // Validaciones de negocio
        validateUsuarioData(usuarioDto);
        // Verificar disponibilidad de datos únicos
        dataAvailabilityService.checkUserDataAvailability(
            usuarioDto.getCedula(), 
            usuarioDto.getCorreo(), 
            usuarioDto.getTelefono()
        );
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
    @Transactional
    public void updatePassword(Integer idUsuario, String newPassword) {
        userValidator.validatePassword(newPassword);
        
        Usuario usuario = findById(idUsuario);
        usuario.setClave(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioDetallesDto getUsuarioDetalles(Integer idUsuario) {

        ValidationUtils.validateId(idUsuario.longValue(), "ID de usuario");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByIdWithCompleteInstitutions(idUsuario);
        
        if (usuarioOpt.isEmpty()) {
            throw new ResourceNotFoundException("Usuario", "id", idUsuario);
        }
        
        Usuario usuario = usuarioOpt.get();
        UsuarioDetallesDto dto = usuarioDetallesMapper.toDto(usuario);
        return dto;
    }

    @Override
    public boolean isCedulaAvailable(String cedula) {
        return dataAvailabilityService.isCedulaAvailable(cedula);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return dataAvailabilityService.isEmailAvailable(email);
    }

    @Override
    public boolean isPhoneAvailable(String phone) {
        return dataAvailabilityService.isPhoneAvailable(phone);
    }

    @Override
    @Transactional
    public void savePhone(Integer idUsuario, String telefono) {
        // Validar el teléfono
        userValidator.validateTelefono(telefono);
        
        // Verificar que el teléfono esté disponible (excluyendo el usuario actual)
        if (!dataAvailabilityService.isPhoneAvailableForUser(telefono, idUsuario)) {
            throw new IllegalArgumentException("El teléfono ya está en uso por otro usuario");
        }
        
        // Buscar el usuario
        Usuario usuario = findById(idUsuario);
        
        // Actualizar el teléfono
        usuario.setTelefono(telefono);
        usuarioRepository.save(usuario);
        
        log.info("Teléfono actualizado exitosamente para el usuario con ID: {}", idUsuario);
    }

    @Override
    @Transactional
    public void saveEmail(Integer idUsuario, String correo) {
        // Validar el correo
        userValidator.validateEmail(correo);
        
        // Verificar que el correo esté disponible (excluyendo el usuario actual)
        if (!dataAvailabilityService.isEmailAvailableForUser(correo, idUsuario)) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso por otro usuario");
        }
        
        // Buscar el usuario
        Usuario usuario = findById(idUsuario);
        
        // Actualizar el correo
        usuario.setCorreo(correo);
        usuarioRepository.save(usuario);
    }

    private void validateUsuarioData(UsuarioDto usuarioDto) {
        userValidator.validateNombres(usuarioDto.getNombres());
        userValidator.validateApellidos(usuarioDto.getApellidos());
        userValidator.validateCedula(usuarioDto.getCedula());
        userValidator.validateEmail(usuarioDto.getCorreo());
        userValidator.validateTelefono(usuarioDto.getTelefono());
        userValidator.validateGenero(usuarioDto.getGenero());
        userValidator.validateFechaNacimiento(usuarioDto.getFecha_nacimiento());
        userValidator.validatePassword(usuarioDto.getClave());
    }

    @Override
    public PageResponseDto<UsuarioConInstitucionesDto> getUsuariosConInstituciones(
            int page, int size, String search, String sortBy, String sortDir) {
        
        List<String> allowedSortFields = Arrays.asList("id", "nombres", "apellidos", "cedula", "correo", "fechaNacimiento");
        Pageable pageable = ValidationUtils.createPageable(page, size, sortBy, sortDir, allowedSortFields);
        
        String normalizedSearch = ValidationUtils.normalizeSearchTerm(search);
        // Ejecutar consulta
        Page<Usuario> usuariosPage = searchUsuariosWithInstituciones(normalizedSearch, pageable);
        // Cargar las relaciones manualmente para los usuarios obtenidos
        List<Usuario> usuariosConRelaciones = loadUserRelations(usuariosPage.getContent());
        // Crear nueva página con las relaciones cargadas
        Page<Usuario> usuariosPageConRelaciones = new PageImpl<>(
                usuariosConRelaciones, 
                pageable, 
                usuariosPage.getTotalElements()
        );
        
        // Mapear a DTO
        return usuarioConInstitucionesMapper.toPageDto(usuariosPageConRelaciones);
    }

    /*  UsuarioInstituciones Repository  */

    @Override
    public List<UsuarioInstitucionDto> getUsuarioInstituciones(Integer idUsuario) {
        ValidationUtils.validateId(idUsuario.longValue(), "ID de usuario");
        return usuarioInstitucionRepository.findActiveInstitutionsByUserId(idUsuario).stream().map(usuarioInstitucionMapper::toDto).collect(Collectors.toList());
    }


    /*  Funciones auxiliares  */

    private Page<Usuario> searchUsuariosWithInstituciones(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return usuarioRepository.findUsuariosWithInstitucionesBySearch(search.trim(), pageable);
        } else {
            return usuarioRepository.findAllUsuariosWithInstituciones(pageable);
        }
    }
    
    private List<Usuario> loadUserRelations(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(usuario -> usuarioRepository.findByCedulaWithRoles(usuario.getCedula()).orElse(usuario))
                .collect(Collectors.toList());
    }

}
