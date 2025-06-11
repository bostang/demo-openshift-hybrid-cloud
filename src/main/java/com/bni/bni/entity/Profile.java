package com.bni.bni.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Entitas yang merepresentasikan data profil pengguna dalam sistem
 * Terhubung dengan tabel 'profiles' di database
 */
@Entity
@Table(name = "profiles")
public class Profile {

    /**
     * ID unik sebagai primary key
     * Dibangkitkan secara otomatis oleh database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relasi one-to-one dengan entitas User
     * Menggunakan user_id sebagai foreign key
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * Nama depan pengguna
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Nama belakang pengguna
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Tempat lahir pengguna
     */
    @Column(name = "place_of_birth")
    private String placeOfBirth;

    /**
     * Tanggal lahir pengguna
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Waktu pembuatan record
     * Diisi otomatis saat pertama kali dibuat
     */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * Waktu terakhir update record
     * Diisi otomatis saat pertama kali dibuat dan setiap update
     */
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // ============== CONSTRUCTORS ==============
    
    /**
     * Constructor default yang diperlukan oleh JPA
     */
    public Profile() {}

    // ============== GETTERS AND SETTERS ==============

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /**
     * @return User yang terkait dengan profil ini
     */
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    /**
     * @return Waktu pembuatan record profil
     */
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * @return Waktu terakhir update record profil
     */
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}