package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, String> {
}
