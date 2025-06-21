package com.nutrizulia.catalog.repository;

import com.nutrizulia.catalog.model.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnfermedadRepository extends JpaRepository<Enfermedad, Integer> {
}
