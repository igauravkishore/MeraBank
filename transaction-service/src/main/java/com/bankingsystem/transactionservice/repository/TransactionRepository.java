package com.bankingsystem.transactionservice.repository;

import com.bankingsystem.transactionservice.model.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByFromAccount(String fromAccount);
    List<Transaction> findByToAccount(String toAccount);
}
