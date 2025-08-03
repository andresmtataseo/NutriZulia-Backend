package com.nutrizulia.features.catalog.repository;

import com.nutrizulia.features.catalog.model.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnfermedadRepository extends JpaRepository<Enfermedad, Integer> {
}
