package com.nutrizulia.features.auth.repository;

import com.nutrizulia.features.auth.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Repositorio para manejar operaciones de la blacklist de tokens JWT
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {

    /**
     * Verifica si un token está en la blacklist
     * @param tokenId ID del token a verificar
     * @return true si el token está en la blacklist, false en caso contrario
     */
    boolean existsByTokenId(String tokenId);

    /**
     * Elimina tokens expirados de la blacklist para optimizar el almacenamiento
     * @param currentDateTime Fecha y hora actual
     * @return Número de registros eliminados
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM TokenBlacklist t WHERE t.expirationDate < :currentDateTime")
    int deleteExpiredTokens(@Param("currentDateTime") LocalDateTime currentDateTime);

    /**
     * Invalida todos los tokens de un usuario específico
     * Útil para logout de todas las sesiones
     * @param userCedula Cédula del usuario
     * @return Número de tokens invalidados
     */
    @Query("SELECT COUNT(t) FROM TokenBlacklist t WHERE t.userCedula = :userCedula")
    long countByUserCedula(@Param("userCedula") String userCedula);
}