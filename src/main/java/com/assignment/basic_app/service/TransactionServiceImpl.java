package com.assignment.basic_app.service;

import com.assignment.basic_app.entity.Transaction;
import com.assignment.basic_app.repo.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Page<Transaction> searchTransactions(String customerId, String accountNumber, String description, Pageable pageable) {
        Specification<Transaction> specification = Specification.where(null);

        if (customerId != null && !customerId.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("customerId"), customerId));
        }

        if (accountNumber != null && !accountNumber.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("accountNumber"), accountNumber));
        }

        if (description != null && !description.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("description"), "%" + description + "%"));
        }
        return transactionRepository.findAll(specification, pageable);
    }

    @Override
    public void updateDescription(Long transactionId, String newDescription) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));

        transaction.setDescription(newDescription);
        transactionRepository.save(transaction);
    }
}
