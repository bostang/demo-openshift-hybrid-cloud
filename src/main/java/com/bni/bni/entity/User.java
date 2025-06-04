package com.bni.bni.entity;

import java.time.OffsetDateTime;
import jakarta.persistence.*;

/**
 * Entity class yang merepresentasikan tabel users dalam database.
 * Menyimpan informasi dasar pengguna seperti kredensial, role, dan waktu pembuatan.
 */
@Entity
@Table(name = "users")  // Secara eksplisit menentukan nama tabel
public class User {

    // Konstanta untuk role default
    public static final String DEFAULT_ROLE = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role = DEFAULT_ROLE;  // Memberikan nilai default

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /**
     * Constructor default diperlukan untuk JPA.
     */
    public User() {
        // Constructor sengaja dikosongkan
    }

    /**
     * Constructor untuk membuat instance User baru.
     * 
     * @param username    Nama pengguna (unik)
     * @param passwordHash Password yang sudah di-hash
     * @param role        Role pengguna (default: USER)
     * @param createdAt   Timestamp waktu pembuatan akun
     */
    public User(String username, String passwordHash, String role, OffsetDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : DEFAULT_ROLE;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
    }

    // --- Getter dan Setter ---

    public Long getId() {
        return id;
    }

    /**
     * @param id ID unik pengguna
     * @NOTE: Sebaiknya tidak mengubah ID setelah entity dibuat
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    /**
     * @param username Nama pengguna baru
     * @throws IllegalArgumentException jika username null atau kosong
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash Password yang sudah di-hash
     * @throws IllegalArgumentException jika passwordHash null atau kosong
     */
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash tidak boleh kosong");
        }
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    /**
     * @param role Role pengguna (USER, ADMIN, dll)
     * @throws IllegalArgumentException jika role null atau kosong
     */
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role tidak boleh kosong");
        }
        this.role = role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Timestamp waktu pembuatan
     * @throws IllegalArgumentException jika createdAt null
     * @NOTE: Waktu pembuatan sebaiknya tidak diubah setelah entity dibuat
     */
    public void setCreatedAt(OffsetDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt tidak boleh null");
        }
        this.createdAt = createdAt;
    }
}