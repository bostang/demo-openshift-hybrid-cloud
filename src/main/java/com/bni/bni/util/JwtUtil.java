package com.bni.bni.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class untuk operasi JWT (JSON Web Token)
 * Menangani pembuatan, validasi, dan parsing token JWT
 */
@Component // Menandai class ini sebagai Spring Component
public class JwtUtil {
    // Secret key untuk signing JWT (dibuat secara otomatis dengan algoritma HS256)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Waktu kadaluarsa token (10 jam dalam miliseconds)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    /**
     * Membuat JWT token untuk user yang berhasil login
     * @param username username pengguna
     * @param role role pengguna
     * @return String JWT token yang sudah di-sign
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username) // Set subject (biasanya username/userId)
                .claim("role", role) // Menambahkan custom claim untuk role
                .setIssuedAt(new Date()) // Waktu pembuatan token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Waktu kadaluarsa
                .signWith(key) // Sign token dengan secret key
                .compact(); // Generate token string
    }

    /**
     * Memvalidasi JWT token
     * @param token JWT token yang akan divalidasi
     * @return true jika token valid, false jika tidak valid/expired
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Catch exception jika token invalid/expired/salah format
            return false;
        }
    }

    /**
     * Mendapatkan username dari JWT token
     * @param token JWT token
     * @return username dari subject token
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject(); // Mendapatkan subject (username)
    }

    /**
     * Mendapatkan semua claims dari JWT token
     * @param token JWT token
     * @return Semua claims dalam token
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}