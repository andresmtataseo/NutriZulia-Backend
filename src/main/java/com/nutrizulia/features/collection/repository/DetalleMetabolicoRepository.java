package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleAntropometrico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleMetabolicoRepository extends JpaRepository<DetalleAntropometrico, String> {
}
