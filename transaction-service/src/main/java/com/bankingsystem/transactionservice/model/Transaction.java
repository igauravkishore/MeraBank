package com.bankingsystem.transactionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String toAccountNumber;
    private String fromAccountNumber;
    private BigDecimal amount;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private TransactionType TransactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus TransactionStatus;

    public enum TransactionType{
        TRANSFER, DEPOSIT, WITHDRAWAL
    }

    public enum TransactionStatus{
        PENDING, SUCCESS, FAILED
    }
}
