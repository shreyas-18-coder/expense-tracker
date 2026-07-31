package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.BudgetRequest;
import com.shreyas.expensetracker.dto.BudgetResponse;
import com.shreyas.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(Authentication authentication,
                                                       @Valid @RequestBody BudgetRequest request) {
        String userEmail = authentication.getName();
        BudgetResponse saved = budgetService.createBudget(userEmail, request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            Authentication authentication,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        String userEmail = authentication.getName();
        List<BudgetResponse> budgets = budgetService.getBudgetsForMonth(userEmail, month, year);
        return ResponseEntity.ok(budgets);
    }
}