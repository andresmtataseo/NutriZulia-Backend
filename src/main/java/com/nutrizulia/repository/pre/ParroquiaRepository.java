package com.nutrizulia.repository.pre;

import com.nutrizulia.model.pre.Parroquia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParroquiaRepository extends JpaRepository<Parroquia, Integer> {

    List<Parroquia> findAllByMunicipio_Id(Integer municipioId);
}
