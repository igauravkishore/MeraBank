package com.bankingsystem.accountservice.service;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final WebClient userServiceWebClient;
    private final CacheManager cacheManager;

    public String generateAccountNumber() {
        String accountNumber;
        do{
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        }while(accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    public Account createAccount(Account account) {
        Long customerId = account.getCustomerId();
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
                    .uri("/customers/id/{customerId}",customerId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }catch (WebClientException ex){
            throw new RuntimeException("Customer not found for id " + customerId);
        }catch (Exception ex){
            throw new RuntimeException("Error verifying user: " + ex.getMessage());
        }

        account.setAccountNumber(generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        Account savedAccount = accountRepository.save(account);
        cacheManager.getCache("accounts").put(savedAccount.getAccountNumber(), savedAccount);
        cacheManager.getCache("accountBalances").put(savedAccount.getAccountNumber(), savedAccount.getBalance());
        return savedAccount;
    }

    public List<Account> findByCustomerId(Long userId) {
        return accountRepository.findByCustomerId(userId);
    }

    @Cacheable(value = "accounts", key = "#accountNumber")
    public Account findByAccountNumber(String accountNumber) {
        System.out.println("Fetching account from DB: " + accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new RuntimeException("Account not found for account number: " + accountNumber);
        }
        return account;
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

    @CachePut(value = "accounts", key = "#accountNumber")
    public Account deposit(String accountNumber, BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new RuntimeException("Account not found");
        }

        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        account.setBalance(currentBalance.add(amount));
        account.setUpdatedAt(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        cacheManager.getCache("accountBalances").put(accountNumber, updatedAccount.getBalance());
        return updatedAccount;
    }

    @CachePut(value = "accounts", key = "#accountNumber")
    public Account withdraw(String accountNumber,  BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new RuntimeException("Account not found");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());

        Account updatedAccount = accountRepository.save(account);
        cacheManager.getCache("accountBalances").put(accountNumber, updatedAccount.getBalance());
        return updatedAccount;
    }

    @Cacheable(value = "accountBalances", key = "#accountNumber")
     public BigDecimal getBalance(String accountNumber){
        System.out.println("Fetching balance from DB for account: " + accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new RuntimeException("Account not found");
        }
        return account.getBalance();
    }


    @Caching(evict = {
            @CacheEvict(value = "accounts", key = "#accountNumber"),
            @CacheEvict(value = "accountBalances", key = "#accountNumber")
    })
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new RuntimeException("Account not found");
        }
        accountRepository.delete(account);
    }
}
