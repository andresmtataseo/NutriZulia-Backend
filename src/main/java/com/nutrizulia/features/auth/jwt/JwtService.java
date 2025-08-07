package com.nutrizulia.features.auth.jwt;

import com.nutrizulia.features.auth.model.TokenBlacklist;
import com.nutrizulia.features.auth.repository.TokenBlacklistRepository;
import com.nutrizulia.features.user.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpirationInMillis;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationInMillis,
            TokenBlacklistRepository tokenBlacklistRepository
    ) {
        this.jwtExpirationInMillis = jwtExpirationInMillis;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("La clave JWT no es válida o no está en formato Base64", e);
        }
    }

    public String generateToken(Usuario user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("idUsuario", user.getId());
        extraClaims.put("roles", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        // Generar JTI único para el token
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .id(jti) // Agregar JTI al token
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(this.secretKey) // Usa la clave ya procesada
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ========== MÉTODOS PARA BLACKLIST ==========

    /**
     * Obtiene el ID único del token (JTI)
     * @param token Token JWT
     * @return ID del token
     */
    public String getTokenId(String token) {
        return getClaim(token, Claims::getId);
    }

    /**
     * Verifica si un token está en la blacklist
     * @param token Token JWT a verificar
     * @return true si está en blacklist, false en caso contrario
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            String tokenId = getTokenId(token);
            return tokenId != null && tokenBlacklistRepository.existsByTokenId(tokenId);
        } catch (Exception e) {
            // Si hay error al procesar el token, considerarlo como inválido
            return true;
        }
    }

    /**
     * Agrega un token a la blacklist
     * @param token Token JWT a invalidar
     * @param userCedula Cédula del usuario propietario del token
     */
    public void blacklistToken(String token, String userCedula) {
        try {
            String tokenId = getTokenId(token);
            Date expiration = getExpiration(token);
            
            if (tokenId != null && expiration != null) {
                LocalDateTime expirationDateTime = expiration.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                
                TokenBlacklist blacklistEntry = new TokenBlacklist(tokenId, userCedula, expirationDateTime);
                tokenBlacklistRepository.save(blacklistEntry);
            }
        } catch (Exception e) {
            // Log del error pero no fallar el logout
            System.err.println("Error al agregar token a blacklist: " + e.getMessage());
        }
    }

}