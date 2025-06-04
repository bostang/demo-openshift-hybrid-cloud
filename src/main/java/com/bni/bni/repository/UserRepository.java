package com.bni.bni.repository;

import com.bni.bni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk mengelola entitas User dalam database.
 * Menyediakan operasi CRUD dasar melalui JpaRepository dan query khusus untuk User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Mencari user berdasarkan username.
     *
     * @param username Nama pengguna yang dicari (case-sensitive)
     * @return Optional yang mengandung User jika ditemukan, empty jika tidak
     * @implNote Query di-generate otomatis oleh Spring Data JPA berdasarkan nama method
     */
    Optional<User> findByUsername(String username);

    /**
     * Memeriksa apakah username sudah terdaftar dalam sistem.
     *
     * @param username Nama pengguna yang akan diperiksa (case-sensitive)
     * @return true jika username sudah ada, false jika belum
     * @implNote Berguna untuk validasi saat registrasi user baru
     */
    boolean existsByUsername(String username);
    
}