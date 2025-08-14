package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetallePediatrico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePediatricoRepository extends JpaRepository<DetallePediatrico, String> {
    
    @Query("SELECT d FROM DetallePediatrico d " +
           "WHERE d.isDeleted = false " +
           "AND d.consulta.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY d.updatedAt DESC")
    List<DetallePediatrico> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
