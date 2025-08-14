package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.EvaluacionAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionAntropometricaRepository extends JpaRepository<EvaluacionAntropometrica, String> {

    @Query("SELECT d FROM EvaluacionAntropometrica d " +
            "WHERE d.isDeleted = false " +
            "AND d.consulta.usuarioInstitucion.institucion.id IN :institutionIds " +
            "ORDER BY d.updatedAt DESC")
    List<EvaluacionAntropometrica> findAllActiveByInstitutionIds(@Param("institutionIds") List<Integer> institutionIds);

}
