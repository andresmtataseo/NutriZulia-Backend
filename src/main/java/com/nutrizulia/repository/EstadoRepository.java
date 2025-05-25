package com.nutrizulia.repository;

import com.nutrizulia.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository  extends JpaRepository<Estado, Integer> {

    List<Estado> findByIdOrNombreContainingIgnoreCase(Integer id, String nombre);

    List<Estado> findByNombreContainingIgnoreCase(String nombre);
}
