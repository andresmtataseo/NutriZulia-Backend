package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.PacienteRepresentante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepresentanteRepository extends JpaRepository<PacienteRepresentante, String> {
    
    @Query("SELECT pr FROM PacienteRepresentante pr " +
            "WHERE pr.isDeleted = false " +
            "AND pr.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY pr.updatedAt DESC")
    List<PacienteRepresentante> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);
}
