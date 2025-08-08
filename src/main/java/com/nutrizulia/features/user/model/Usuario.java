package com.nutrizulia.features.user.model;

import com.nutrizulia.common.enums.Genero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "cedula", nullable = false, length = 10, unique = true)
    private String cedula;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @Column(name = "telefono", length = 12, unique = true)
    private String telefono;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "clave", nullable = false)
    private String clave;

    @ColumnDefault("true")
    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UsuarioInstitucion> usuarioInstituciones;

    // Métodos de Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.usuarioInstituciones == null) {
            return List.of();
        }
        return this.usuarioInstituciones.stream()
                .filter(ui -> ui.getRol() != null && ui.getRol().getNombre() != null)
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
    public boolean isEnabled() { return this.isEnabled != null && this.isEnabled; }
}
