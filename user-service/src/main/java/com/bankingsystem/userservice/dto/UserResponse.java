package com.bankingsystem.userservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String keycloakId;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
