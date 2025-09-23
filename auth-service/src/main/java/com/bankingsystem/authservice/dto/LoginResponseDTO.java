package com.bankingsystem.authservice.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class LoginResponseDTO {
    private final String token;

}
