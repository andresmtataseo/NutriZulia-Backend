package com.nutrizulia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "cedula", nullable = false, length = 20, unique = true)
    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 20, message = "La cédula no debe exceder los 20 caracteres")
    @Schema(description = "Cédula del usuario", example = "V-12345678", required = true)
    private String cedula;

    @Column(name = "nombres", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombres del usuario", example = "Juan Carlos", required = true)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    @NotBlank(message = "Los apellidos son obligatorios")
    @Schema(description = "Apellidos del usuario", example = "Pérez Rodríguez", required = true)
    private String apellidos;

    @Column(name = "telefono", nullable = false, length = 20, unique = true)
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres")
    @Schema(description = "Teléfono del usuario", example = "04121234567", required = true)
    private String telefono;

    @Column(name = "correo", nullable = false, unique = true)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@dominio.com", required = true)
    private String correo;

    @Column(name = "clave", nullable = false)
    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "123456", required = true)
    private String clave;

    @Column(name = "is_enabled", nullable = false)
    @Schema(description = "Indica si el usuario está habilitado", example = "true")
    private boolean isEnabled = true;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Schema(description = "Lista de relaciones del usuario con instituciones")
    private List<UsuarioInstitucion> usuarioInstituciones;

    // Métodos de Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.usuarioInstituciones.stream()
                .map(ui -> new SimpleGrantedAuthority(ui.getRol().getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.clave;
    }

    @Override
    public String getUsername() {
        return this.cedula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
