package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.EvaluacionAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionAntropometricaRepository extends JpaRepository<EvaluacionAntropometrica, String> {
}
