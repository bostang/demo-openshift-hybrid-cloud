package com.bni.bni.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity class yang merepresentasikan tabel users dalam database.
 * Menyimpan informasi dasar pengguna termasuk kredensial dan role.
 */
@Entity
@Table(name = "users") // Menentukan nama tabel di database
public class User {

    /************* KOLOM : id **************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    /************* KOLOM : username **************/
    @Column(nullable = false, unique = true) // Tidak boleh null dan harus unik
    private String username; // Nama pengguna untuk login

    /************* KOLOM : password_hash **************/
    @Column(name = "password_hash", nullable = false) // Kolom di database bernama password_hash
    private String passwordHash; // Penyimpanan hash password (bukan password plaintext)

    /************* KOLOM : role **************/
    @Column(nullable = false) // Tidak boleh null
    private String role; // Role pengguna (misal: ADMIN, USER)

    /************* KOLOM : created_at **************/
    @Column(name = "created_at", nullable = false) // Kolom timestamp pembuatan user
    private OffsetDateTime createdAt; // Waktu pembuatan akun dengan timezone offset

    /************* KOLOM : email_address **************/
    @Column(nullable = true) // boleh null
    private String emailAddress; // email pengguna

    /************* KOLOM : updated_at **************/
    @Column(name = "updated_at", nullable = false) // Kolom timestamp pembuatan user
    private OffsetDateTime updatedAt; // Waktu pembuatan akun dengan timezone offset

    /************* KOLOM : is_active **************/
    @Column(name = "is_active", nullable = false) // Kolom timestamp pembuatan user
    private boolean isActive; // Waktu pembuatan akun dengan timezone offset

    /**
     * Default constructor diperlukan oleh JPA.
     */
    public User() {
        // Constructor kosong untuk JPA
    }

    /**
     * Constructor untuk membuat instance User baru.
     * 
     * @param username Nama pengguna (unik)
     * @param passwordHash Hash dari password pengguna
     * @param role Role/tingkat akses pengguna
     * @param createdAt Waktu pembuatan akun
     */
    public User(String username, String passwordHash, String role, OffsetDateTime createdAt, String emailAddress, OffsetDateTime updatedAt, boolean isActive) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
        this.emailAddress = emailAddress;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    // Berikut adalah getter dan setter untuk semua field
    // Getter digunakan untuk mengakses nilai field
    // Setter digunakan untuk mengubah nilai field

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public OffsetDateTime getupdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }
}