package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Representante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, String> {
    
    @Query("SELECT r FROM Representante r WHERE r.isDeleted = false AND r.usuarioInstitucion.institucion.id IN :institutionIds ORDER BY r.updatedAt DESC")
    List<Representante> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
