package com.nutrizulia.features.collection.repository;

import com.nutrizulia.features.collection.model.DetalleMetabolico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMetabolicoRepository extends JpaRepository<DetalleMetabolico, String> {

    @Query("SELECT d FROM DetalleMetabolico d " +
            "WHERE d.isDeleted = false " +
            "AND d.consulta.usuarioInstitucion.usuario.id = :userId " +
            "ORDER BY d.updatedAt DESC")
    List<DetalleMetabolico> findAllActiveByUserId(@Param("userId") Integer userId);
}
