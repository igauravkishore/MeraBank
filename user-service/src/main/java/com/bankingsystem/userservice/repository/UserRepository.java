package com.bankingsystem.userservice.repository;

import com.bankingsystem.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByKeycloakId(String keycloakId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
