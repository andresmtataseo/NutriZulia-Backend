package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, String> {
    
    @Query("SELECT p FROM Consulta p " +
            "WHERE p.isDeleted = false " +
            "AND p.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY p.updatedAt DESC")
    List<Consulta> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
