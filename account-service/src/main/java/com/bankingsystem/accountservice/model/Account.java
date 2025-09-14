package com.bankingsystem.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private Long customerId;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT
    }

}
