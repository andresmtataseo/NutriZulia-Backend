package com.nutrizulia.catalog.repository;

import com.nutrizulia.catalog.model.Parroquia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParroquiaRepository extends JpaRepository<Parroquia, Integer> {

    List<Parroquia> findAllByMunicipio_Id(Integer municipioId);
}
