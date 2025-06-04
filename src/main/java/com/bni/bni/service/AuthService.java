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
 * Service untuk menangani operasi otentikasi seperti registrasi dan login pengguna.
 */
@Service
public class AuthService {
    
    // Dependency injection untuk komponen yang diperlukan
    @Autowired
    private UserRepository userRepository;  // Mengganti nama 'repo' menjadi lebih deskriptif
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Mendaftarkan pengguna baru ke sistem.
     * 
     * @param username Nama pengguna yang akan didaftarkan
     * @param password Password pengguna (belum dienkripsi)
     * @return Pesan status registrasi
     * @TODO: Sebaiknya menggunakan custom exception atau response object daripada string
     * @TODO: Password belum dienkripsi dan disimpan ke database
     */
    public String register(String username, String password) {
        // Validasi apakah username sudah terdaftar
        if (userRepository.existsByUsername(username)) {
            return "User sudah ada!";
        }

        // Membuat entitas user baru
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setRole("USER");  // Role default untuk user baru
        newUser.setCreatedAt(OffsetDateTime.now());
        
        // @TODO: Seharusnya password dienkripsi sebelum disimpan
        // newUser.setPasswordHash(passwordEncoder.encode(password));
        
        userRepository.save(newUser);

        return "Registrasi berhasil!";
    }

    /**
     * Melakukan proses login pengguna dan mengembalikan token JWT jika berhasil.
     * 
     * @param username Nama pengguna
     * @param password Password (belum dienkripsi)
     * @return Token JWT jika login berhasil, null jika gagal
     * @TODO: Sebaiknya menggunakan custom exception untuk kasus login gagal
     */
    public String login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Verifikasi password jika user ditemukan
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // @TODO: Perlu memastikan passwordHash tidak null sebelum memverifikasi
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return jwtUtil.generateToken(username, user.getRole());
            }
        }

        return null;  // Login gagal
    }
}