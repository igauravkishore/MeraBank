package com.bankingsystem.customerservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
