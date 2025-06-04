package com.bni.bni.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfigurasi Security Filter Chain untuk mengatur akses HTTP dan autentikasi.
     * 
     * @param http Objek HttpSecurity untuk konfigurasi
     * @return SecurityFilterChain yang telah dikonfigurasi
     * @throws Exception jika terjadi error saat konfigurasi
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Nonaktifkan CSRF protection (biasanya untuk API stateless)
            .csrf(csrf -> csrf.disable())
            
            // Izinkan semua request tanpa autentikasi
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // Nonaktifkan autentikasi basic HTTP
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // Nonaktifkan form login
            .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Bean untuk password encoder menggunakan BCrypt hashing.
     * 
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}