package com.nutrizulia.features.auth.service;

import com.nutrizulia.features.auth.repository.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Servicio para la limpieza automática de tokens expirados de la blacklist
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    /**
     * Limpia tokens expirados de la blacklist
     * Se ejecuta cada 6 horas (21600000 ms)
     */
    @Scheduled(fixedRate = 21600000) // 6 horas
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            log.info("Iniciando limpieza de tokens expirados...");
            
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = tokenBlacklistRepository.deleteExpiredTokens(now);
            
            log.info("Limpieza completada. Tokens eliminados: {}", deletedCount);
            
        } catch (Exception e) {
            log.error("Error durante la limpieza de tokens expirados", e);
        }
    }

    /**
     * Limpieza manual de tokens expirados
     * @return Número de tokens eliminados
     */
    @Transactional
    public int manualCleanup() {
        try {
            log.info("Iniciando limpieza manual de tokens expirados...");
            
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = tokenBlacklistRepository.deleteExpiredTokens(now);
            
            log.info("Limpieza manual completada. Tokens eliminados: {}", deletedCount);
            return deletedCount;
            
        } catch (Exception e) {
            log.error("Error durante la limpieza manual de tokens expirados", e);
            return 0;
        }
    }
}