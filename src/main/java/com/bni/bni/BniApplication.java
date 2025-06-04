package com.bni.bni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kelas utama yang menjadi entry point untuk aplikasi Spring Boot.
 * Menggunakan anotasi @SpringBootApplication yang menggabungkan tiga anotasi penting:
 * 1. @Configuration - Menandai kelas sebagai sumber definisi bean
 * 2. @EnableAutoConfiguration - Mengaktifkan konfigurasi otomatis Spring Boot
 * 3. @ComponentScan - Memungkinkan scanning komponen dalam package ini dan sub-packages
 */
@SpringBootApplication
public class BniApplication {

    /**
     * Method utama yang akan dijalankan saat aplikasi pertama kali di-start.
     * 
     * @param args Argumen command line yang bisa diteruskan ke aplikasi
     */
    public static void main(String[] args) {
        // Memulai aplikasi Spring Boot dengan konfigurasi default
        SpringApplication.run(BniApplication.class, args);
    }

}