package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleObstetricia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleObstetriciaRepository extends JpaRepository<DetalleObstetricia, String> {
}
