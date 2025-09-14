package com.bankingsystem.transactionservice.controller;

import com.bankingsystem.transactionservice.Dtos.TransferRequest;
import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/transfer")
    public ResponseEntity<?> createTransaction(@RequestBody TransferRequest transactionRequest) {
        try {
            Transaction savedTransaction = transactionService.processTransfer(
                    transactionRequest.getFromAccountNumber(),
                    transactionRequest.getToAccountNumber(),
                    transactionRequest.getAmount()
            );

            return ResponseEntity.ok(savedTransaction);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transaction failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{fromAccountNumber}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByFromAccount(@PathVariable String fromAccountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByFromAccount(fromAccountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/toAccountNumber")
    public ResponseEntity<List<Transaction>> getAllTransactionsByToAccountNumber(@RequestParam String toAccountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByToAccount(toAccountNumber);
        return ResponseEntity.ok(transactions);
    }

}
