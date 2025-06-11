package com.bni.bni.controller;

import com.bni.bni.service.ProfileService;
import com.bni.bni.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller untuk mengelola operasi terkait profil pengguna
 */
@RestController
@RequestMapping("/api")
public class ProfileController {

    // Dependency injection untuk ProfileService dan JwtUtil
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor untuk dependency injection
     * @param profileService service untuk operasi profil
     * @param jwtUtil utility untuk operasi JWT
     */
    public ProfileController(ProfileService profileService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Endpoint untuk memperbarui data profil pengguna
     * @param authHeader Header Authorization yang berisi token JWT
     * @param requestBody Request body yang berisi data profil yang akan diupdate
     * @return ResponseEntity yang berisi status dan pesan response
     */
    @PostMapping("/me/update")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {
        
        // Map untuk menyimpan response
        Map<String, Object> response = new HashMap<>();

        try {
            // Validasi token
            String token = authHeader.replace("Bearer ", "").trim();
            if (!jwtUtil.validateToken(token)) {
                response.put("status", 401);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }

            // Ekstrak user_id dari token
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("user_id", Long.class);

            // Parse data dari request body
            String firstName = requestBody.get("first_name");
            String lastName = requestBody.get("last_name");
            String placeOfBirth = requestBody.get("place_of_birth");
            LocalDate dateOfBirth = LocalDate.parse(requestBody.get("date_of_birth"));

            // Panggil service untuk update profil
            String result = profileService.updateProfile(
                userId, firstName, lastName, placeOfBirth, dateOfBirth);

            // Response sukses
            response.put("status", 200);
            response.put("message", result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle error dan return response error
            response.put("status", 400);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint untuk mendapatkan data profil pengguna
     * @param authHeader Header Authorization yang berisi token JWT
     * @return ResponseEntity yang berisi status dan data profil
     */
    @GetMapping("/me/profile")
    public ResponseEntity<Map<String, Object>> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        
        // Map untuk menyimpan response
        Map<String, Object> response = new HashMap<>();

        try {
            // Validasi token
            String token = authHeader.replace("Bearer ", "").trim();
            if (!jwtUtil.validateToken(token)) {
                response.put("status", 401);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }

            // Ekstrak user_id dari token
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            Long userId = claims.get("user_id", Long.class);

            // Panggil service untuk mendapatkan data profil
            Map<String, Object> profileData = profileService.getProfileData(userId);

            // Response sukses
            response.put("status", 200);
            response.put("data", profileData);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle error dan return response error
            response.put("status", 400);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}