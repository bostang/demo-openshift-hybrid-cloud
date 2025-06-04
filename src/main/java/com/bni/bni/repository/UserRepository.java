package com.bni.bni.repository;

import com.bni.bni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface untuk mengakses data User dari database.
 * Menyediakan operasi CRUD dasar melalui JpaRepository dan query khusus untuk entitas User.
 */
@Repository // Menandai interface ini sebagai Spring Data Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Mencari user berdasarkan username.
     * Menggunakan Optional untuk menghindari NullPointerException.
     * 
     * @param username Nama pengguna yang dicari
     * @return Optional yang berisi User jika ditemukan, atau empty jika tidak
     */
    Optional<User> findByUsername(String username);

    /**
     * Memeriksa apakah username sudah terdaftar dalam sistem.
     * 
     * @param username Nama pengguna yang akan diperiksa
     * @return true jika username sudah ada, false jika belum
     */
    boolean existsByUsername(String username);
}