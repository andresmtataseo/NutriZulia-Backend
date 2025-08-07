package com.nutrizulia.features.user.repository;

import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioInstitucionRepository extends JpaRepository<UsuarioInstitucion, Integer> {

    List<UsuarioInstitucion> findAllByUsuario_Id(Integer usuarioId);

    UsuarioInstitucion findAllByUsuario_IdAndInstitucion_IdAndFechaFinBeforeAndIsEnabled(Integer usuarioId, Integer institucionId, LocalDate fechaFinBefore, Boolean isEnabled);

    @Query("SELECT ui FROM UsuarioInstitucion ui " +
            "WHERE ui.usuario.id = :usuarioId " +
            "AND ui.institucion.id = 1 " +
            "AND ui.rol.id IN (1, 2) " +
            "AND (ui.fechaFin IS NULL OR ui.fechaFin >= CURRENT_DATE) " +
            "AND ui.isEnabled = true")
    Optional<UsuarioInstitucion> findActiveUserInstitutionWithSpecificRoles(
            @Param("usuarioId") Integer usuarioId);
}