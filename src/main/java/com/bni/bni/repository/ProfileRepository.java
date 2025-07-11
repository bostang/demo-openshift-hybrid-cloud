package com.bni.bni.repository;

import com.bni.bni.entity.Profile;
import com.bni.bni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}