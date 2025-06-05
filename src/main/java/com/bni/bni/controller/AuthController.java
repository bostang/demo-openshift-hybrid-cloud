package com.bni.bni.controller;

import com.bni.bni.service.AuthService;
import com.bni.bni.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;

/**
 * Controller untuk menangani autentikasi pengguna termasuk registrasi, login, dan validasi token.
 * Menggunakan JWT (JSON Web Token) untuk otentikasi.
 */
@RestController
@RequestMapping("/api/auth") // Base path untuk semua endpoint dalam controller ini
public class AuthController {

    @Value("${CONFIG_MAP_VALUE:default-config}") // Nilai diambil dari config map, default jika tidak ada
    private String configMapValue;

    @Value("${SECRET_VALUE:default-secret}") // Nilai diambil dari secret, default jika tidak ada
    private String secretValue;

    @Autowired
    private AuthService authService; // Service untuk logika bisnis autentikasi

    @Autowired
    private JwtUtil jwtUtil; // Utility untuk operasi JWT (generasi, validasi, parsing)

    /**
     * Endpoint untuk registrasi pengguna baru.
     * 
     * @param body Request body yang berisi username dan password
     * @return ResponseEntity dengan status dan pesan registrasi
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String emailAddress = body.get("email_address");
        String password = body.get("password");
        String message = authService.register(username, emailAddress, password);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk login pengguna.
     * 
     * @param body Request body yang berisi username dan password
     * @return ResponseEntity dengan status dan token JWT jika berhasil,
     *         atau pesan error jika gagal
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String emailAddress = body.get("email_address");
        String password = body.get("password");
        String token = authService.login(username, emailAddress, password);

        Map<String, Object> response = new HashMap<>();
        if (token != null) {
            response.put("status", 200);
            response.put("token", token);
            // menambahkan message : "Login berhasil"
            response.put("message", "Login berhasil!!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", 401);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * Endpoint untuk mendapatkan informasi pengguna yang sedang login.
     * Memvalidasi token JWT dari header Authorization.
     * 
     * @param authHeader Header Authorization yang berisi token JWT
     * @return ResponseEntity dengan informasi pengguna jika token valid,
     *         atau pesan error jika token tidak valid/tidak ada
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();

        // Validasi format header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("status", 400);
            response.put("message", "Authorization header missing or invalid");
            return ResponseEntity.status(400).body(response);
        }

        try {
            String token = authHeader.replace("Bearer ", "").trim();

            // Validasi token JWT
            if (!jwtUtil.validateToken(token)) {
                response.put("status", 401);
                response.put("message", "Token tidak valid atau expired");
                return ResponseEntity.status(401).body(response);
            }

            // Ekstrak claims dari token JWT
            Claims claims = jwtUtil.getAllClaimsFromToken(token);

            // Membuat response dengan informasi dari token
            response.put("status", 200);
            response.put("username", claims.getSubject()); // Subjek token biasanya berisi username
            response.put("role", claims.get("role")); // Role pengguna
            response.put("issuedAt", claims.getIssuedAt()); // Waktu pembuatan token
            response.put("expiration", claims.getExpiration()); // Waktu kadaluarsa token
            response.put("config_map", configMapValue); // Nilai dari config map
            response.put("secret", secretValue); // Nilai dari secret

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", 401);
            response.put("message", "Token tidak valid");
            return ResponseEntity.status(401).body(response);
        }
    }
}