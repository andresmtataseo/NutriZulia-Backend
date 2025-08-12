package com.nutrizulia.features.user.controller;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.*;
import com.nutrizulia.features.user.dto.UpdateUsuarioDto;
import com.nutrizulia.features.user.dto.UpdateUsuarioInstitucionDto;
import com.nutrizulia.features.user.service.IUsuarioService;
import com.nutrizulia.features.user.service.IUsuarioInstitucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(USERS_BASE_URL)
@Tag(
        name = "Gestión de Usuarios",
        description = "Operaciones para crear, editar, desactivar usuarios, asignar instituciones de salud y roles."
)
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IUsuarioInstitucionService usuarioInstitucionService;

    @Operation(summary = "Crear un nuevo usuario", 
               description = "Crea un nuevo usuario en el sistema con validaciones completas. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o errores de validación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un usuario con los mismos datos únicos (cédula, correo o teléfono)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(USERS_CREATE)
    public ResponseEntity<ApiResponseDto<UsuarioDto>> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioCreado = usuarioService.createUsuario(usuarioDto);
        ApiResponseDto<UsuarioDto> response = ApiResponseDto.<UsuarioDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Usuario obtenido exitosamente")
                .data(usuarioCreado)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Verificar disponibilidad de cédula", description = "Verifica si una cédula está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro cédula inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_CEDULA)
    public ResponseEntity<Boolean> checkCedulaAvailability(@RequestParam String cedula) {
        boolean isAvailable = usuarioService.isCedulaAvailable(cedula);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Verificar disponibilidad de correo electrónico", 
               description = "Verifica si un correo electrónico está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro email inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_EMAIL)
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = usuarioService.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Verificar disponibilidad de teléfono", 
               description = "Verifica si un número de teléfono está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro phone inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_PHONE)
    public ResponseEntity<Boolean> checkPhoneAvailability(@RequestParam String phone) {
        boolean isAvailable = usuarioService.isPhoneAvailable(phone);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Obtener usuarios con instituciones paginado", 
               description = "Devuelve una lista paginada de usuarios con sus instituciones y roles. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_GET_ALL_WITH_INSTITUTIONS)
    public ResponseEntity<PageResponseDto<UsuarioConInstitucionesDto>> getUsuariosConInstituciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "nombres") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        PageResponseDto<UsuarioConInstitucionesDto> response = usuarioService
                .getUsuariosConInstituciones(page, size, search, sortBy, sortDir);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener detalles completos de un usuario", 
               description = "Devuelve un usuario con información completa de sus instituciones y roles asignados. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDetallesDto.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_GET_DETAIL)
    public ResponseEntity<ApiResponseDto<UsuarioDetallesDto>> getUsuarioDetail(@RequestParam Integer idUsuario) {
        UsuarioDetallesDto usuarioDetalles = usuarioService.getUsuarioDetalles(idUsuario);
        ApiResponseDto<UsuarioDetallesDto> response = ApiResponseDto.<UsuarioDetallesDto>builder()
                .status(200)
                .message("Usuario obtenido exitosamente")
                .data(usuarioDetalles)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar teléfono del usuario", 
               description = "Actualiza el número de teléfono de un usuario existente con validaciones correspondientes. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o teléfono ya en uso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PutMapping(USERS_SAVE_PHONE)
    public ResponseEntity<ApiResponseDto<Void>> savePhone(@Valid @RequestBody SavePhoneDto savePhoneDto) {
        usuarioService.savePhone(savePhoneDto.getIdUsuario(), savePhoneDto.getTelefono());
        
        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .status(200)
                .message("Teléfono actualizado exitosamente")
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar correo electrónico del usuario", 
               description = "Actualiza el correo electrónico de un usuario existente con validaciones correspondientes. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo electrónico actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo ya en uso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PutMapping(USERS_SAVE_EMAIL)
    public ResponseEntity<ApiResponseDto<Void>> saveEmail(@Valid @RequestBody SaveEmailDto saveEmailDto) {
        usuarioService.saveEmail(saveEmailDto.getIdUsuario(), saveEmailDto.getCorreo());
        
        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .status(200)
                .message("Correo electrónico actualizado exitosamente")
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar datos de un usuario", 
               description = "Actualiza los datos de un usuario existente con validaciones completas. Excluye la contraseña. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o errores de validación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe otro usuario con los mismos datos únicos (cédula, correo o teléfono)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PutMapping(USERS_UPDATE)
    public ResponseEntity<ApiResponseDto<UsuarioDto>> updateUsuario(
            @RequestParam Integer idUsuario,
            @Valid @RequestBody UpdateUsuarioDto updateUsuarioDto) {
        
        UsuarioDto usuarioActualizado = usuarioService.updateUsuario(idUsuario, updateUsuarioDto);
        
        ApiResponseDto<UsuarioDto> response = ApiResponseDto.<UsuarioDto>builder()
                .status(HttpStatus.OK.value())
                .message("Usuario actualizado exitosamente")
                .data(usuarioActualizado)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener lista de las instituciones por usuario",
            description = "Devuelve un usuario con sus instituciones y roles asignados. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioInstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USER_INSTITUTIONS_GET_BY_USER)
    public ResponseEntity<ApiResponseDto<List<UsuarioInstitucionDto>>> getUsuariosInstituciones(@RequestParam Integer idUsuario) {

        List<UsuarioInstitucionDto> usuarios = usuarioService.getUsuarioInstituciones(idUsuario);

        ApiResponseDto<List<UsuarioInstitucionDto>> response = ApiResponseDto.<List<UsuarioInstitucionDto>>builder()
                .status(200)
                .message("Usuario obtenido exitosamente")
                .data(usuarios)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Asignar usuario a institución", 
               description = "Asigna un usuario a una institución con un rol específico. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario asignado exitosamente a la institución", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioInstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o errores de validación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario, institución o rol no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - El usuario ya está asignado a esta institución con este rol", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(USERS_ASSIGN_INSTITUTION)
    public ResponseEntity<ApiResponseDto<UsuarioInstitucionDto>> assignUserToInstitution(
            @Valid @RequestBody CreateUsuarioInstitucionDto createUsuarioInstitucionDto) {
        
        UsuarioInstitucionDto usuarioInstitucionCreado = usuarioInstitucionService.createUsuarioInstitucion(createUsuarioInstitucionDto);
        
        ApiResponseDto<UsuarioInstitucionDto> response = ApiResponseDto.<UsuarioInstitucionDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Usuario asignado exitosamente a la institución")
                .data(usuarioInstitucionCreado)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar asignación de usuario a institución", 
               description = "Actualiza el rol y estado de una asignación existente de usuario a institución. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignación actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioInstitucionDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o errores de validación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Asignación o rol no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PutMapping(USERS_UPDATE_INSTITUTION)
    public ResponseEntity<ApiResponseDto<UsuarioInstitucionDto>> updateUserInstitutionAssignment(
            @RequestParam Integer id,
            @Valid @RequestBody UpdateUsuarioInstitucionDto updateUsuarioInstitucionDto) {
        
        UsuarioInstitucionDto usuarioInstitucionActualizado = usuarioInstitucionService.updateUsuarioInstitucion(id, updateUsuarioInstitucionDto);
        
        ApiResponseDto<UsuarioInstitucionDto> response = ApiResponseDto.<UsuarioInstitucionDto>builder()
                .status(HttpStatus.OK.value())
                .message("Asignación actualizada exitosamente")
                .data(usuarioInstitucionActualizado)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

}
