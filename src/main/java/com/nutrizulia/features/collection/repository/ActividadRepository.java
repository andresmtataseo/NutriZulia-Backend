package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, String> {
    
    @Query("SELECT p FROM Actividad p " +
            "WHERE p.isDeleted = false " +
            "AND p.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY p.updatedAt DESC")
    List<Actividad> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
