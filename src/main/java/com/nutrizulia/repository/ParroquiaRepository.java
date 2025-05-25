package com.nutrizulia.repository;

import com.nutrizulia.model.Parroquia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParroquiaRepository extends JpaRepository<Parroquia, Integer> {

    List<Parroquia> findByMunicipioIdAndIdAndNombreContainingIgnoreCase(Integer municipioId, Integer id, String nombre);

    List<Parroquia> findByMunicipioIdAndNombreContainingIgnoreCase(Integer municipioId, String nombre);

    Optional<Parroquia> findByMunicipioIdAndId(Integer municipioId, Integer id);

    List<Parroquia> findByMunicipioId(Integer municipioId);
}
