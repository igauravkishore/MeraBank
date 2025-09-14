package com.bankingsystem.accountservice.controller;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import com.bankingsystem.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts") //from /api/accounts -> /accounts
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;


    @PostMapping("/createAccount/{customerId}")
    public ResponseEntity<?> createAccount(@RequestBody Account account,
                                           @PathVariable Long customerId) {
        if (accountRepository.existsByCustomerId(customerId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account already exists for userId: " + customerId);
        }
        try {
            account.setCustomerId(customerId);
            Account savedAccount = accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        try{
            Account depositedAccount = accountService.deposit(accountNumber, amount);
            return ResponseEntity.ok(depositedAccount);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        try {
            Account withdrawedAccount = accountService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(withdrawedAccount);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAlAccounts() {
      return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Long customerId) {
        try{
            List<Account> accounts = accountService.findByCustomerId(customerId);
            if(accounts.isEmpty()) {
                return ResponseEntity.notFound().build();
            }else return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalanceByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Account> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

}
