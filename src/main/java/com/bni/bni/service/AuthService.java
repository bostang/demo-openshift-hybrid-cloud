package com.bni.bni.service;

import com.bni.bni.entity.User;
import com.bni.bni.repository.UserRepository;
import com.bni.bni.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Service class untuk menangani logika bisnis terkait autentikasi
 * termasuk registrasi dan login pengguna
 */
@Service // Menandai class ini sebagai Spring Service
public class AuthService {

    @Autowired // Dependency injection untuk UserRepository
    private UserRepository repo;

    @Autowired // Dependency injection untuk PasswordEncoder (biasanya BCrypt)
    private PasswordEncoder encoder;

    @Autowired // Dependency injection untuk JwtUtil (JWT utilities)
    private JwtUtil jwtUtil;

    /**
     * Method untuk registrasi pengguna baru
     * @param username username pengguna
     * @param password password dalam plaintext
     * @return Pesan status registrasi
     */
    public String register(String username, String emailAddress, String password) {
        // Cek apakah username sudah terdaftar
        if (repo.existsByUsername(username)) {
            return "User already exists";
        }

        // Buat user baru
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encoder.encode(password)); // Encode password sebelum disimpan
        user.setRole("USER"); // Set default role
        user.setCreatedAt(OffsetDateTime.now()); // Set waktu pembuatan
        user.setEmailAddress(emailAddress); // Set email address
        user.setIsActive(true); // Set isActive
        user.setUpdatedAt(OffsetDateTime.now()); // Set waktu pembuatan
        repo.save(user); // Simpan ke database

        return "Registered successfully";
    }

    /**
     * Method untuk proses login pengguna
     * @param username username pengguna
     * @param password password dalam plaintext
     * @param emailAddress email address yang dimasukkan
     * @return Token JWT jika login berhasil, null jika gagal
     */
    /*
    public String login(String username, String emailAddress, String password) {
        // Cari user berdasarkan username
        Optional<User> user = repo.findByUsername(username);
        
        // Verifikasi password + emailAddress dan generate token jika valid
        if (user.isPresent() && encoder.matches(password, user.get().getPasswordHash()) && (user.get().getEmailAddress().equals(emailAddress))) {
            return jwtUtil.generateToken(username, user.get().getRole());
        }

        return null; // Return null jika autentikasi gagal
    }
    */
    public String login(String username, String emailAddress, String password) {
        Optional<User> user = repo.findByUsername(username);
        
        if (user.isPresent() && 
            encoder.matches(password, user.get().getPasswordHash()) && 
            user.get().getEmailAddress().equals(emailAddress)) {
            
            return jwtUtil.generateToken(user.get());
        }
        return null;
    }
}