package com.nutrizulia.repository;

import com.nutrizulia.model.Usuario;
import com.nutrizulia.model.UsuarioInstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioInstitucionRepository extends JpaRepository<UsuarioInstitucion, Integer> {

    List<UsuarioInstitucion> findByUsuario(Usuario usuario);
}