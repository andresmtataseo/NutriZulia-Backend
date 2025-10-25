package com.nutrizulia.features.user.repository;

import com.nutrizulia.features.user.model.Usuario;
import com.nutrizulia.features.user.model.UsuarioInstitucion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT ui FROM UsuarioInstitucion ui " +
            "WHERE ui.usuario.id = :usuarioId " +
            "AND ui.institucion.id != 1 " +
            "AND ui.isEnabled = true")
    List<UsuarioInstitucion> findActiveInstitutionsByUserId(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT ui FROM UsuarioInstitucion ui " +
            "WHERE ui.usuario.id = :usuarioId " +
            "AND ui.institucion.id != 1 ")
    List<UsuarioInstitucion> findInstitutionsByUserId(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT COUNT(ui) > 0 FROM UsuarioInstitucion ui " +
            "WHERE ui.usuario.id = :usuarioId " +
            "AND ui.institucion.id = :institucionId " +
            "AND ui.rol.id = :rolId " +
            "AND ui.isEnabled = true " +
            "AND (ui.fechaFin IS NULL OR ui.fechaFin >= CURRENT_DATE)")
    boolean existsActiveAssignment(@Param("usuarioId") Integer usuarioId,
                                  @Param("institucionId") Integer institucionId,
                                  @Param("rolId") Integer rolId);

    @Query("SELECT COUNT(ui) > 0 FROM UsuarioInstitucion ui " +
            "WHERE ui.usuario.id = :usuarioId " +
            "AND ui.institucion.id = :institucionId " +
            "AND ui.isEnabled = true " +
            "AND (ui.fechaFin IS NULL OR ui.fechaFin >= CURRENT_DATE)")
    boolean existsActiveUserInInstitution(@Param("usuarioId") Integer usuarioId,
                                         @Param("institucionId") Integer institucionId);

    @Query("SELECT ui FROM UsuarioInstitucion ui " +
           "LEFT JOIN FETCH ui.usuario u " +
           "LEFT JOIN FETCH ui.rol r " +
           "WHERE ui.institucion.id = :institucionId " +
           "AND ui.isEnabled = true " +
           "AND (ui.fechaFin IS NULL OR ui.fechaFin >= CURRENT_DATE)")
    List<UsuarioInstitucion> findActiveUsersByInstitucionId(@Param("institucionId") Integer institucionId);

    @Query(value = "SELECT ui FROM UsuarioInstitucion ui " +
           "LEFT JOIN FETCH ui.usuario u " +
           "LEFT JOIN FETCH ui.rol r " +
           "WHERE ui.institucion.id IN :institucionIds " +
           "AND ui.isEnabled = true " +
           "AND (ui.fechaFin IS NULL OR ui.fechaFin >= CURRENT_DATE)")
    List<UsuarioInstitucion> findActiveUsersByInstitucionIds(@Param("institucionIds") List<Integer> institucionIds);

    @Query(value = "SELECT ui FROM UsuarioInstitucion ui " +
           "LEFT JOIN FETCH ui.usuario u " +
           "LEFT JOIN FETCH ui.rol r " +
           "WHERE ui.institucion.id IN :institucionIds")
    List<UsuarioInstitucion> findAllUsersByInstitucionIds(@Param("institucionIds") List<Integer> institucionIds);
}