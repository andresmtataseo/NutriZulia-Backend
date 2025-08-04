package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleMetabolico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleMetabolicoRepository extends JpaRepository<DetalleMetabolico, String> {
}
