package com.bni.bni.controller;

import com.bni.bni.service.AuthService;
import com.bni.bni.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Controller untuk menangani operasi otentikasi seperti registrasi, login, dan validasi token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Konstanta untuk pesan response
    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String TOKEN_KEY = "token";
    private static final String USERNAME_KEY = "username";
    private static final String ROLE_KEY = "role";

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint untuk registrasi pengguna baru.
     * 
     * @param body Map berisi username dan password
     * @return ResponseEntity dengan status dan pesan registrasi
     * @apiNote POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get(USERNAME_KEY);
        String password = body.get("password");
        
        // Validasi input
        if (username == null || password == null) {
            return buildErrorResponse(400, "Username dan password harus diisi");
        }

        String message = authService.register(username, password);
        
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS_KEY, 200);
        response.put(MESSAGE_KEY, message);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint untuk proses login pengguna.
     * 
     * @param body Map berisi username dan password
     * @return ResponseEntity dengan token JWT jika berhasil
     * @apiNote POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get(USERNAME_KEY);
        String password = body.get("password");
        
        // Validasi input
        if (username == null || password == null) {
            return buildErrorResponse(400, "Username dan password harus diisi");
        }

        String token = authService.login(username, password);
        
        if (token != null) {
            Map<String, Object> response = new HashMap<>();
            response.put(STATUS_KEY, 200);
            response.put(TOKEN_KEY, token);
            return ResponseEntity.ok(response);
        } else {
            return buildErrorResponse(401, "Username atau password salah");
        }
    }

    /**
     * Endpoint untuk mendapatkan informasi pengguna yang sedang login.
     * 
     * @param authHeader Header Authorization berisi Bearer token
     * @return ResponseEntity dengan data pengguna jika token valid
     * @apiNote GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // Validasi header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return buildErrorResponse(400, "Authorization header tidak valid");
        }

        try {
            String token = authHeader.replace("Bearer ", "").trim();
            
            if (!jwtUtil.validateToken(token)) {
                return buildErrorResponse(401, "Token tidak valid atau expired");
            }

            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            
            Map<String, Object> response = new HashMap<>();
            response.put(STATUS_KEY, 200);
            response.put(USERNAME_KEY, claims.getSubject());
            response.put(ROLE_KEY, claims.get(ROLE_KEY));
            response.put("issuedAt", claims.getIssuedAt());
            response.put("expiration", claims.getExpiration());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return buildErrorResponse(401, "Token tidak valid: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk membangun response error.
     * 
     * @param statusCode Kode status HTTP
     * @param message Pesan error
     * @return ResponseEntity dengan format error standar
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(int statusCode, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS_KEY, statusCode);
        response.put(MESSAGE_KEY, message);
        return ResponseEntity.status(statusCode).body(response);
    }
}