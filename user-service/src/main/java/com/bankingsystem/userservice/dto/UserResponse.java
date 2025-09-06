package com.bankingsystem.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
