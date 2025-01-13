package com.assignment.basic_app.repo;

import com.assignment.basic_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByAccountNumber(String accountNumber);
}
