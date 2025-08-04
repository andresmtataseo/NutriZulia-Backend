package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.PacienteRepresentante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepresentanteRepository extends JpaRepository<PacienteRepresentante, String> {
}
