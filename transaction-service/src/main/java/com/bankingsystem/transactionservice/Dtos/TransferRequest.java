package com.bankingsystem.transactionservice.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}
