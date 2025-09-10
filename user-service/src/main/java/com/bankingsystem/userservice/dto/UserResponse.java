package com.bankingsystem.userservice.dto;

import com.bankingsystem.userservice.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Set<Role> roles;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
