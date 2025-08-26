package com.bankingsystem.accountservice.repository;

import com.bankingsystem.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByUserId(String userId);

    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
}
