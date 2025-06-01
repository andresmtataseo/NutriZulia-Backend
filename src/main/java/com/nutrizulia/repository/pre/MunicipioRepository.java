package com.nutrizulia.repository.pre;

import com.nutrizulia.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findAllByEstado_Id(Integer estadoId);
}
