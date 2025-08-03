package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
}
