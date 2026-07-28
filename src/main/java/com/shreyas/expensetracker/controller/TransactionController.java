package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.TransactionRequest;
import com.shreyas.expensetracker.dto.TransactionResponse;
import com.shreyas.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(Authentication authentication,
                                                                 @Valid @RequestBody TransactionRequest request) {
        String userEmail = authentication.getName();
        TransactionResponse saved = transactionService.createTransaction(userEmail, request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getTransactionsForUser(userEmail);
        return ResponseEntity.ok(transactions);
    }
}