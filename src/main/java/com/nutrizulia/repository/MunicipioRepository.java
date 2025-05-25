package com.nutrizulia.repository;

import com.nutrizulia.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findByEstadoIdAndIdAndNombreContainingIgnoreCase(Integer estadoId, Integer id, String nombre);

    Optional<Municipio> findByEstadoIdAndId(Integer estadoId, Integer id);

    List<Municipio> findByEstadoIdAndNombreContainingIgnoreCase(Integer estadoId, String nombre);

    List<Municipio> findByEstadoId(Integer estadoId);

}
