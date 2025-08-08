package com.nutrizulia.features.user.repository;

import com.nutrizulia.features.user.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCedula(String cedula);

     @Query("SELECT u FROM Usuario u " +
             "LEFT JOIN FETCH u.usuarioInstituciones ui " +
             "LEFT JOIN FETCH ui.rol " +
             "WHERE u.cedula = :cedula")
     Optional<Usuario> findByCedulaWithRoles(@Param("cedula") String cedula);

     @Query(value = "SELECT DISTINCT u FROM Usuario u " +
            "WHERE u.isEnabled = true",
            countQuery = "SELECT COUNT(DISTINCT u) FROM Usuario u WHERE u.isEnabled = true")
     Page<Usuario> findAllUsuariosWithInstituciones(Pageable pageable);

     @Query(value = "SELECT DISTINCT u FROM Usuario u " +
            "WHERE u.isEnabled = true " +
            "AND (LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.cedula) LIKE LOWER(CONCAT('%', :search, '%')) " +
             "OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(DISTINCT u) FROM Usuario u " +
            "WHERE u.isEnabled = true " +
            "AND (LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.cedula) LIKE LOWER(CONCAT('%', :search, '%')) " +
             "OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :search, '%')))")
     Page<Usuario> findUsuariosWithInstitucionesBySearch(@Param("search") String search, Pageable pageable);

}
