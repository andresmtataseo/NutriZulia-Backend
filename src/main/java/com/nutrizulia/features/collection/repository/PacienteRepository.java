package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    
    @Query("SELECT p FROM Paciente p " +
            "WHERE p.isDeleted = false " +
            "AND p.usuarioInstitucion.institucion.id IN :institucionIds " +
            "ORDER BY p.updatedAt DESC")
    List<Paciente> findAllActiveByInstitutionIds(@Param("institucionIds") List<Integer> institucionIds);

    @Query("SELECT p FROM Paciente p " +
            "WHERE p.isDeleted = false " +
            "AND p.usuarioInstitucion.usuario.id = :userId " +
            "ORDER BY p.updatedAt DESC")
    List<Paciente> findAllActiveByUserId(@Param("userId") Integer userId);
}
