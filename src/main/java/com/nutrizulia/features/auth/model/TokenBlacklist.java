package com.nutrizulia.features.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar tokens JWT invalidados (blacklist)
 * Permite invalidar tokens antes de su expiración natural
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist implements Serializable {

    @Id
    @Column(name = "token_id", nullable = false, length = 36)
    private String tokenId; // JTI del token JWT

    @Column(name = "user_cedula", nullable = false, length = 10)
    private String userCedula;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "blacklisted_at", nullable = false)
    @Builder.Default
    private LocalDateTime blacklistedAt = LocalDateTime.now();

    @Column(name = "reason", length = 100)
    @Builder.Default
    private String reason = "LOGOUT";

    /**
     * Constructor para crear una entrada de blacklist
     * @param tokenId ID único del token (JTI)
     * @param userCedula Cédula del usuario propietario del token
     * @param expirationDate Fecha de expiración del token
     */
    public TokenBlacklist(String tokenId, String userCedula, LocalDateTime expirationDate) {
        this.tokenId = tokenId;
        this.userCedula = userCedula;
        this.expirationDate = expirationDate;
        this.blacklistedAt = LocalDateTime.now();
        this.reason = "LOGOUT";
    }
}