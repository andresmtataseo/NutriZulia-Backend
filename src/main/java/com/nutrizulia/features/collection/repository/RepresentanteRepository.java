package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Representante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, String> {
}
