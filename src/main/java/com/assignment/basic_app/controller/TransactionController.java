package com.assignment.basic_app.controller;

import com.assignment.basic_app.dto.TransactionUpdateDTO;
import com.assignment.basic_app.entity.Transaction;
import com.assignment.basic_app.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<Transaction>> getTransactions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(transactionService.searchTransactions(customerId, accountNumber, description, pageable));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransactionDescription(
            @PathVariable Long transactionId,
            @RequestBody @Valid TransactionUpdateDTO dto) {
        transactionService.updateDescription(transactionId, dto.getDescription());
        return ResponseEntity.noContent().build();
    }
}
