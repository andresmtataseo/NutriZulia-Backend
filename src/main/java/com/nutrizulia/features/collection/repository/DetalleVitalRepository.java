package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVitalRepository extends JpaRepository<DetalleVital, String> {
    
    @Query("SELECT dv FROM DetalleVital dv " +
            "WHERE dv.isDeleted = false " +
            "AND dv.consulta.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY dv.updatedAt DESC")
    List<DetalleVital> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
