package com.nutrizulia.features.user.controller;

import com.nutrizulia.common.dto.PageResponseDto;
import com.nutrizulia.features.user.dto.UsuarioConInstitucionesDto;
import com.nutrizulia.features.user.dto.UsuarioDto;
import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.user.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nutrizulia.common.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USERS_BASE_URL)
@Tag(
        name = "Gestión de Usuarios",
        description = "Operaciones para crear, editar, desactivar usuarios, asignar instituciones de salud y roles."
)
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @Operation(summary = "Obtener lista de usuarios de salud", description = "Devuelve una lista de usuarios de salud. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "400", description = "Faltan parámetros requeridos o son inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado - El recurso solicitado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_GET_ALL)
    public ResponseEntity<List<UsuarioDto>> getUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Crear un nuevo usuario", 
               description = "Crea un nuevo usuario en el sistema con validaciones completas. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o errores de validación", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un usuario con los mismos datos únicos (cédula, correo o teléfono)", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @PostMapping(USERS_CREATE)
    public ResponseEntity<UsuarioDto> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioCreado = usuarioService.createUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @Operation(summary = "Verificar disponibilidad de cédula", 
               description = "Verifica si una cédula está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro cédula inválido", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_CEDULA)
    public ResponseEntity<Boolean> checkCedulaAvailability(@RequestParam String cedula) {
        boolean isAvailable = usuarioService.isCedulaAvailable(cedula);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Verificar disponibilidad de correo electrónico", 
               description = "Verifica si un correo electrónico está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro email inválido", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_EMAIL)
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = usuarioService.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Verificar disponibilidad de teléfono", 
               description = "Verifica si un número de teléfono está disponible para registro. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación exitosa", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(type = "boolean", example = "true"))),
            @ApiResponse(responseCode = "400", description = "Parámetro phone inválido", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
    })
    @GetMapping(USERS_CHECK_PHONE)
    public ResponseEntity<Boolean> checkPhoneAvailability(@RequestParam String phone) {
        boolean isAvailable = usuarioService.isPhoneAvailable(phone);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Obtener usuarios con instituciones paginado", 
               description = "Devuelve una lista paginada de usuarios con sus instituciones y roles. **Requiere autenticación.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - La autenticación es requerida o ha fallado.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los permisos necesarios para acceder a este recurso.", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                        content = @Content(mediaType = "application/json", 
                        schema = @Schema(implementation = ApiResponseDto.class)))
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

}
