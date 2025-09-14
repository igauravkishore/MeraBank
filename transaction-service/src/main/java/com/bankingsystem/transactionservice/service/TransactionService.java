package com.bankingsystem.transactionservice.service;

import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final RestClient restClient;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String ACCOUNT_URL = "http://localhost:8082/accounts";

    public TransactionService(RestClient restClient, TransactionRepository transactionRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.restClient = restClient;
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Transaction processTransfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setFromAccountNumber(fromAccountNumber);
        transaction.setToAccountNumber(toAccountNumber);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());

        boolean withdrawDone = false;
        try{
            restClient.put()
                    .uri(ACCOUNT_URL + "/{fromAccountNumber}/withdraw?amount={amount}", fromAccountNumber, amount)
                    .retrieve()
                    .toBodilessEntity();
                    withdrawDone = true;

            restClient.post()
                    .uri(ACCOUNT_URL + "/{toAccountNumber}/deposit?amount={amount}", toAccountNumber, amount)
                    .retrieve()
                    .toBodilessEntity();
            transaction.setTransactionStatus(Transaction.TransactionStatus.SUCCESS);
            kafkaTemplate.send("transaction-events", "Transfer of " + amount + " from " + fromAccountNumber + " to " + toAccountNumber
                    + " successful.");
            return transactionRepository.save(transaction);

        }catch(Exception e){
            if(withdrawDone) {
                try {
                    restClient.post()
                            .uri(ACCOUNT_URL + "{fromAccount}/deposit?amount={amount}", fromAccountNumber, amount)
                            .retrieve()
                            .toBodilessEntity();
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            kafkaTemplate.send("transaction-events", "Transfer of " + amount + " from " + fromAccountNumber + " to " + toAccountNumber + " is failed.");
            throw new RuntimeException("Transfer failed. Rollback performed: " + e.getMessage());
        }
    }


    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByFromAccount(String fromAccount) {
        return transactionRepository.findByFromAccountNumber(fromAccount);
    }

    public List<Transaction> getTransactionsByToAccount(String toAccount) {
        return transactionRepository.findByToAccountNumber(toAccount);
    }
}
