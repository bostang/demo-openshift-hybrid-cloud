package com.bni.bni.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Konfigurasi keamanan untuk aplikasi Spring.
 * Menggunakan Spring Security untuk mengatur autentikasi dan otorisasi.
 */
@Configuration
@EnableWebSecurity // Mengaktifkan konfigurasi keamanan web Spring Security
public class SecurityConfig {

    /**
     * Mendefinisikan filter chain untuk keamanan HTTP.
     * Konfigurasi ini menonaktifkan beberapa fitur keamanan default
     * dan mengizinkan semua request tanpa autentikasi.
     * 
     * @param http Objek HttpSecurity untuk konfigurasi
     * @return SecurityFilterChain yang dikonfigurasi
     * @throws Exception jika terjadi kesalahan selama konfigurasi
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Menonaktifkan CSRF protection (Cross-Site Request Forgery)
            .csrf(csrf -> csrf.disable())
            
            // Mengatur autorisasi request - mengizinkan semua request tanpa autentikasi
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // Menonaktifkan autentikasi HTTP Basic
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // Menonaktifkan form login default
            .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Mendefinisikan password encoder yang akan digunakan aplikasi.
     * Menggunakan BCrypt hashing algorithm untuk keamanan password.
     * 
     * @return instance BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}