package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleObstetricia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleObstetriciaRepository extends JpaRepository<DetalleObstetricia, String> {

    @Query("SELECT d FROM DetalleObstetricia d " +
            "WHERE d.isDeleted = false " +
            "AND d.consulta.usuarioInstitucion.usuario.id = :userId " +
            "ORDER BY d.updatedAt DESC")
    List<DetalleObstetricia> findAllActiveByUserId(@Param("userId") Integer userId);
}
