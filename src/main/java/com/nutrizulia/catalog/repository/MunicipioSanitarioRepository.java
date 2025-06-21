package com.nutrizulia.catalog.repository;

import com.nutrizulia.catalog.model.MunicipioSanitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioSanitarioRepository extends JpaRepository<MunicipioSanitario, Integer> {
    List<MunicipioSanitario> findAllByEstado_Id(Integer estadoId);
}
