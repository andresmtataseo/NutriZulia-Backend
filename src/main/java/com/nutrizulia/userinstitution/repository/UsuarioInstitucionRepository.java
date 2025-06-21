package com.nutrizulia.userinstitution.repository;

import com.nutrizulia.userinstitution.model.UsuarioInstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioInstitucionRepository extends JpaRepository<UsuarioInstitucion, Integer> {

    List<UsuarioInstitucion> findAllByUsuario_Id(Integer usuarioId);
}