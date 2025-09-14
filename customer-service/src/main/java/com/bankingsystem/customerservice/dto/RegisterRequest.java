package com.bankingsystem.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;


@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;


}
