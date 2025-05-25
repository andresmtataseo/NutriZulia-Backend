package com.nutrizulia.repository;

import com.nutrizulia.model.UsuarioInstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioInstitucionRepository extends JpaRepository<UsuarioInstitucion, Integer> {
}