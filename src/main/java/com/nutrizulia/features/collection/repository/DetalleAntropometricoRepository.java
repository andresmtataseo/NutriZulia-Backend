package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleAntropometrico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleAntropometricoRepository extends JpaRepository<DetalleAntropometrico, String> {

    @Query("SELECT da FROM DetalleAntropometrico da " +
            "WHERE da.isDeleted = false " +
            "AND da.consulta.usuarioInstitucion.usuario.id = :userId " +
            "ORDER BY da.updatedAt DESC")
    List<DetalleAntropometrico> findAllActiveByUserId(@Param("userId") Integer userId);
}
