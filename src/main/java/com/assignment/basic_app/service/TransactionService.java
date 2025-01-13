package com.assignment.basic_app.service;

import com.assignment.basic_app.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    public void updateDescription(Long transactionId, String newDescription);
    public Page<Transaction> searchTransactions(String customerId, String accountNumber, String description, Pageable pageable);
}
