package com.bni.bni.service;

import com.bni.bni.entity.Profile;
import com.bni.bni.entity.User;
import com.bni.bni.repository.ProfileRepository;
import com.bni.bni.repository.UserRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service layer untuk operasi terkait profil pengguna
 * Menangani logika bisnis untuk manajemen profil
 */
@Service
public class ProfileService {

    // Dependency repositories untuk akses data profile dan user
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    /**
     * Constructor untuk dependency injection
     * @param profileRepository repository untuk entitas Profile
     * @param userRepository repository untuk entitas User
     */
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Memperbarui data profil pengguna
     * @param userId ID pengguna yang akan diupdate profilnya
     * @param firstName Nama depan
     * @param lastName Nama belakang
     * @param placeOfBirth Tempat lahir
     * @param dateOfBirth Tanggal lahir
     * @return Pesan status hasil operasi update
     */
    public String updateProfile(Long userId, String firstName, String lastName,
                              String placeOfBirth, LocalDate dateOfBirth) {
        
        // Cari user berdasarkan ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();
        // Cari profil yang sudah ada atau buat baru jika belum ada
        Optional<Profile> profileOptional = profileRepository.findByUser(user);

        // Gunakan profil yang ada atau buat baru
        Profile profile = profileOptional.orElseGet(() -> {
            Profile newProfile = new Profile();
            newProfile.setUser(user);
            return newProfile;
        });

        // Update data profil
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setPlaceOfBirth(placeOfBirth);
        profile.setDateOfBirth(dateOfBirth);
        profile.setUpdatedAt(OffsetDateTime.now());

        // Simpan perubahan ke database
        profileRepository.save(profile);
        return "Profile updated successfully";
    }

    /**
     * Mengambil data profil pengguna
     * @param userId ID pengguna yang akan diambil datanya
     * @return Map berisi data profil atau Map kosong jika tidak ditemukan
     */
    public Map<String, Object> getProfileData(Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        // Cari user berdasarkan ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return response; // Return map kosong jika user tidak ditemukan
        }

        // Cari profil berdasarkan user
        Optional<Profile> profileOptional = profileRepository.findByUser(userOptional.get());
        if (profileOptional.isEmpty()) {
            return response; // Return map kosong jika profil tidak ditemukan
        }

        // Masukkan data profil ke dalam response
        Profile profile = profileOptional.get();
        response.put("first_name", profile.getFirstName());
        response.put("last_name", profile.getLastName());
        response.put("place_of_birth", profile.getPlaceOfBirth());
        response.put("date_of_birth", profile.getDateOfBirth().toString());
        response.put("updated_at", profile.getUpdatedAt().toString());

        return response;
    }
}