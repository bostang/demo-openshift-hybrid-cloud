package com.bni.bni.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class untuk operasi JWT (JSON Web Token).
 * Menangani pembuatan, validasi, dan ekstraksi informasi dari token JWT.
 */
@Component
public class JwtUtil {
    // Konstanta untuk konfigurasi JWT
    private static final String ROLE_CLAIM = "role";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    
    // Key untuk signing JWT (di-generate secara otomatis)
    private final Key key = Keys.secretKeyFor(SIGNATURE_ALGORITHM);
    
    // Waktu kedaluwarsa token (10 jam dalam milidetik)
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 10;

    /**
     * Membuat JWT token untuk user yang terautentikasi.
     *
     * @param username Username pengguna
     * @param role Role pengguna
     * @return Token JWT yang sudah ditandatangani
     * @throws IllegalArgumentException jika username atau role null/empty
     */
    public String generateToken(String username, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role tidak boleh kosong");
        }

        return Jwts.builder()
                .setSubject(username)
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key)
                .compact();
    }

    /**
     * Memvalidasi integritas JWT token.
     *
     * @param token Token JWT yang akan divalidasi
     * @return true jika token valid, false jika tidak valid/expired
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // Token sudah expired
            // TODO: Tambahkan logging jika diperlukan
            return false;
        } catch (JwtException | IllegalArgumentException ex) {
            // Token tidak valid
            // TODO: Tambahkan logging jika diperlukan
            return false;
        }
    }

    /**
     * Mengekstrak username dari JWT token.
     *
     * @param token Token JWT
     * @return Username dari token
     * @throws JwtException jika token tidak valid
     */
    public String getUsernameFromToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    /**
     * Mengekstrak semua claims dari JWT token.
     *
     * @param token Token JWT
     * @return Semua claims dalam token
     * @throws JwtException jika token tidak valid
     */
    public Claims getAllClaimsFromToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}