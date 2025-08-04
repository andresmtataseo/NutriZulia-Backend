package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, String> {
}
