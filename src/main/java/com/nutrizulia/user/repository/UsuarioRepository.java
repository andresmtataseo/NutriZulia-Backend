package com.nutrizulia.user.repository;

import com.nutrizulia.user.model.Usuario;
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


}
