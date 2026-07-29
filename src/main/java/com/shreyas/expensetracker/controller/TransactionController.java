package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.TransactionRequest;
import com.shreyas.expensetracker.dto.TransactionResponse;
import com.shreyas.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

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
    public ResponseEntity<Page<TransactionResponse>> getMyTransactions(
            Authentication authentication,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userEmail = authentication.getName();
        Page<TransactionResponse> transactions = transactionService.getTransactionsForUser(
                userEmail, categoryId, startDate, endDate, page, size);
        return ResponseEntity.ok(transactions);
    }
}