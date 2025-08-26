package com.bankingsystem.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(updatable = false, nullable = false)
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
