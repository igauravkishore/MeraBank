package com.bankingsystem.authservice.util;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "guabiuaopqyqvbqlka";

    public String generateToken(String email,String role) {
        return Jwts.builder()
                .subject(email)
                .claim(role,role)
                .issuedAt(LocalDate.now())
    }
}
