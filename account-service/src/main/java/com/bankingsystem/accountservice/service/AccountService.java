package com.bankingsystem.accountservice.service;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final WebClient userServiceWebClient;

    public String generateAccountNumber() {
        String accountNumber;
        do{
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        }while(accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    public Account createAccount(Account account) {
        String userId = account.getUserId();
//        if (userId == null) {
//            throw new IllegalArgumentException("UserId must be provided to create an account.");
//        }

        // Verify the user exists before creating an account
//        try {
//            restClient.get()
//                    .uri("http://user-service/api/users/id/{userId}", userId)
//                    .retrieve()
//                    .toBodilessEntity();
//        } catch (HttpClientErrorException.NotFound ex) {
//            throw new RuntimeException("User profile not found for ID: " + userId);
//        }
        try{
            userServiceWebClient.get()
                    .uri("/api/users/id/{userId}",userId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }catch (WebClientException ex){
            throw new RuntimeException("User not found for id " + userId);
        }

        account.setAccountNumber(generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public List<Account> findByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account findByAccountNumber(String accountNumber) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
        if(account.isEmpty()){
            throw  new RuntimeException("Invalid Account Number");
        }
        return account.get();
    }


//    public Account updateBalance(String accountNumber, BigDecimal amount){
//        Optional<Account> accountOpt = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
//        if (accountOpt.isEmpty()) {
//            throw new RuntimeException("Account not found");
//        }
//        Account account = accountOpt.get();
//        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
//        account.setBalance(currentBalance.add(amount));
//        account.setUpdatedAt(LocalDateTime.now());
//        return accountRepository.save(account);
//    }

    public Account deposit(String AccountNumber, BigDecimal amount){
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByAccountNumber(AccountNumber));
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        account.setBalance(currentBalance.add(amount));
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }


    public Account withdraw(String AccountNumber,BigDecimal amount){
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByAccountNumber(AccountNumber));
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public BigDecimal getBalance(String AccountNumber){
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByAccountNumber(AccountNumber));
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        return accountOptional.get().getBalance();
    }

    public void deleteAccount(String accountNumber) {
        accountRepository.delete(accountRepository.findByAccountNumber(accountNumber));
    }
}
