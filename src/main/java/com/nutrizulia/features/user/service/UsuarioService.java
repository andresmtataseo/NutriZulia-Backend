package com.nutrizulia.features.user.service;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.common.enums.Genero;
import com.nutrizulia.common.exception.ResourceNotFoundException;
import com.nutrizulia.common.service.DataAvailabilityService;
import com.nutrizulia.common.service.EmailService;
import com.nutrizulia.common.util.PasswordUtils;
import com.nutrizulia.common.util.ValidationUtils;
import com.nutrizulia.common.validator.UserValidator;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDetallesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.dto.UpdateUsuarioDto;
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
    private final EmailService emailService;

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
    public UsuarioDto updateUsuario(Integer idUsuario, UpdateUsuarioDto updateUsuarioDto) {
        log.info("Iniciando actualización de usuario con ID: {}", idUsuario);
        
        // Validar ID
        ValidationUtils.validateId(idUsuario.longValue(), "ID de usuario");
        
        // Buscar usuario existente
        Usuario usuarioExistente = findById(idUsuario);
        
        // Validar datos del DTO
        validateUpdateUsuarioData(updateUsuarioDto);
        
        // Verificar disponibilidad de datos únicos (excluyendo el usuario actual)
        validateUniqueDataForUpdate(updateUsuarioDto, idUsuario);
        
        // Verificar si el usuario se está desactivando
        boolean usuarioSeDesactiva = usuarioExistente.getIsEnabled() && !updateUsuarioDto.getIs_enabled();
        
        // Actualizar campos
        usuarioExistente.setCedula(updateUsuarioDto.getCedula());
        usuarioExistente.setNombres(updateUsuarioDto.getNombres());
        usuarioExistente.setApellidos(updateUsuarioDto.getApellidos());
        usuarioExistente.setFechaNacimiento(updateUsuarioDto.getFecha_nacimiento());
        usuarioExistente.setGenero(Genero.valueOf(updateUsuarioDto.getGenero()));
        usuarioExistente.setTelefono(updateUsuarioDto.getTelefono());
        usuarioExistente.setCorreo(updateUsuarioDto.getCorreo());
        usuarioExistente.setIsEnabled(updateUsuarioDto.getIs_enabled());
        
        // Si el usuario se está desactivando, desactivar todas sus asociaciones con instituciones
        if (usuarioSeDesactiva) {
            desactivarTodasLasInstitucionesDelUsuario(idUsuario);
            log.info("Se desactivaron todas las asociaciones de instituciones para el usuario con ID: {}", idUsuario);
        }
        
        // Guardar cambios
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        
        log.info("Usuario actualizado exitosamente con ID: {}", idUsuario);
        
        // Convertir a DTO y retornar
        return usuarioMapper.toDto(usuarioActualizado);
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
        // Validaciones de negocio (excluyendo contraseña si es null o vacía)
        validateUsuarioDataForCreation(usuarioDto);
        
        // Verificar disponibilidad de datos únicos
        dataAvailabilityService.checkUserDataAvailability(
            usuarioDto.getCedula(), 
            usuarioDto.getCorreo(), 
            usuarioDto.getTelefono()
        );
        
        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        
        // Manejar contraseña: generar aleatoria si es null o vacía
        String passwordToUse;
        boolean passwordGenerated = false;
        
        if (usuarioDto.getClave() == null || usuarioDto.getClave().trim().isEmpty()) {
            passwordToUse = PasswordUtils.generateRandomPassword();
            passwordGenerated = true;
            log.info("Contraseña aleatoria generada para usuario con cédula: {}", usuarioDto.getCedula());
        } else {
            passwordToUse = usuarioDto.getClave();
        }
        
        // Encriptar contraseña
        usuario.setClave(passwordEncoder.encode(passwordToUse));
        
        // Establecer valores por defecto
        usuario.setIsEnabled(usuarioDto.getIs_enabled() != null ? usuarioDto.getIs_enabled() : true);
        
        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Si se generó una contraseña aleatoria, enviarla por correo
        if (passwordGenerated) {
            try {
                String nombreCompleto = usuarioGuardado.getNombres() + " " + usuarioGuardado.getApellidos();
                emailService.creacionUsuario(usuarioGuardado.getCorreo(), nombreCompleto, passwordToUse);
                log.info("Correo de creación de usuario enviado exitosamente a: {}", usuarioGuardado.getCorreo());
            } catch (Exception e) {
                log.error("Error al enviar correo de creación de usuario para {}: {}", 
                         usuarioGuardado.getCedula(), e.getMessage());
                // No lanzamos excepción para no fallar la creación del usuario
            }
        }
        
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

    /**
     * Valida los datos del usuario para creación, excluyendo la contraseña si es null o vacía
     */
    private void validateUsuarioDataForCreation(UsuarioDto usuarioDto) {
        userValidator.validateNombres(usuarioDto.getNombres());
        userValidator.validateApellidos(usuarioDto.getApellidos());
        userValidator.validateCedula(usuarioDto.getCedula());
        userValidator.validateEmail(usuarioDto.getCorreo());
        userValidator.validateTelefono(usuarioDto.getTelefono());
        userValidator.validateGenero(usuarioDto.getGenero());
        userValidator.validateFechaNacimiento(usuarioDto.getFecha_nacimiento());
        
        // Solo validar contraseña si no es null o vacía
        if (usuarioDto.getClave() != null && !usuarioDto.getClave().trim().isEmpty()) {
            userValidator.validatePassword(usuarioDto.getClave());
        }
    }

    /**
     * Valida los datos del usuario para actualización
     */
    private void validateUpdateUsuarioData(UpdateUsuarioDto updateUsuarioDto) {
        userValidator.validateNombres(updateUsuarioDto.getNombres());
        userValidator.validateApellidos(updateUsuarioDto.getApellidos());
        userValidator.validateCedula(updateUsuarioDto.getCedula());
        userValidator.validateEmail(updateUsuarioDto.getCorreo());
        userValidator.validateTelefono(updateUsuarioDto.getTelefono());
        userValidator.validateGenero(updateUsuarioDto.getGenero());
        userValidator.validateFechaNacimiento(updateUsuarioDto.getFecha_nacimiento());
    }

    /**
     * Valida que los datos únicos estén disponibles para actualización (excluyendo el usuario actual)
     */
    private void validateUniqueDataForUpdate(UpdateUsuarioDto updateUsuarioDto, Integer idUsuario) {
        // Verificar disponibilidad de cédula (excluyendo el usuario actual)
        if (!dataAvailabilityService.isCedulaAvailableForUser(updateUsuarioDto.getCedula(), idUsuario)) {
            throw new IllegalArgumentException("La cédula ya está en uso por otro usuario");
        }
        
        // Verificar disponibilidad de correo (excluyendo el usuario actual)
        if (!dataAvailabilityService.isEmailAvailableForUser(updateUsuarioDto.getCorreo(), idUsuario)) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso por otro usuario");
        }
        
        // Verificar disponibilidad de teléfono (excluyendo el usuario actual)
        if (!dataAvailabilityService.isPhoneAvailableForUser(updateUsuarioDto.getTelefono(), idUsuario)) {
            throw new IllegalArgumentException("El teléfono ya está en uso por otro usuario");
        }
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

    /**
     * Desactiva todas las asociaciones de instituciones de un usuario
     * @param idUsuario ID del usuario
     */
    private void desactivarTodasLasInstitucionesDelUsuario(Integer idUsuario) {
        log.debug("Desactivando todas las asociaciones de instituciones para el usuario con ID: {}", idUsuario);
        
        // Obtener todas las asociaciones del usuario
        List<UsuarioInstitucion> asociaciones = usuarioInstitucionRepository.findAllByUsuario_Id(idUsuario);
        
        // Desactivar todas las asociaciones que estén activas
        for (UsuarioInstitucion asociacion : asociaciones) {
            if (asociacion.getIsEnabled()) {
                asociacion.setIsEnabled(false);
                // Establecer fecha de fin como la fecha actual
                asociacion.setFechaFin(java.time.LocalDate.now());
                log.debug("Desactivando asociación ID: {} - Usuario: {} - Institución: {} - Fecha fin: {}", 
                         asociacion.getId(), idUsuario, asociacion.getInstitucion().getId(), 
                         asociacion.getFechaFin());
            }
        }
        
        // Guardar todos los cambios
        if (!asociaciones.isEmpty()) {
            usuarioInstitucionRepository.saveAll(asociaciones);
            log.info("Se desactivaron {} asociaciones de instituciones para el usuario con ID: {} con fecha fin establecida", 
                    asociaciones.size(), idUsuario);
        }
    }

}
