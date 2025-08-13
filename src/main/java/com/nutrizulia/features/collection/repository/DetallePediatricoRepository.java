package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetallePediatrico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePediatricoRepository extends JpaRepository<DetallePediatrico, String> {
}
