package com.nutrizulia.features.catalog.repository;

import com.nutrizulia.features.catalog.model.Institucion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {

    @Query(value = "SELECT DISTINCT i FROM Institucion i " +
           "LEFT JOIN FETCH i.municipioSanitario ms " +
           "LEFT JOIN FETCH i.tipoInstitucion ti",
           countQuery = "SELECT COUNT(DISTINCT i) FROM Institucion i")
    Page<Institucion> findAllInstitucionesWithDetails(Pageable pageable);

    @Query(value = "SELECT DISTINCT i FROM Institucion i " +
           "LEFT JOIN FETCH i.municipioSanitario ms " +
           "LEFT JOIN FETCH i.tipoInstitucion ti " +
           "WHERE (LOWER(i.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ms.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ti.nombre) LIKE LOWER(CONCAT('%', :search, '%')))",
           countQuery = "SELECT COUNT(DISTINCT i) FROM Institucion i " +
           "LEFT JOIN i.municipioSanitario ms " +
           "LEFT JOIN i.tipoInstitucion ti " +
           "WHERE (LOWER(i.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ms.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(ti.nombre) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Institucion> findInstitucionesWithDetailsBySearch(@Param("search") String search, Pageable pageable);

    /**
     * Verifica si existe una institución con el nombre especificado (case-insensitive)
     */
    @Query("SELECT COUNT(i) > 0 FROM Institucion i WHERE LOWER(i.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);

    /**
     * Busca una institución por nombre (case-insensitive)
     */
    @Query("SELECT i FROM Institucion i WHERE LOWER(i.nombre) = LOWER(:nombre)")
    Optional<Institucion> findByNombreIgnoreCase(@Param("nombre") String nombre);
}
