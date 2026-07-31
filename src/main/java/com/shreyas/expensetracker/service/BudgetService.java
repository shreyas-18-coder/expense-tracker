package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.dto.BudgetRequest;
import com.shreyas.expensetracker.dto.BudgetResponse;
import com.shreyas.expensetracker.entity.*;
import com.shreyas.expensetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BudgetResponse createBudget(String userEmail, BudgetRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = new Budget();
        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setCategory(category);
        budget.setUser(user);

        Budget saved = budgetRepository.save(budget);

        return toResponse(saved);
    }

    public List<BudgetResponse> getBudgetsForMonth(String userEmail, Integer month, Integer year) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return budgetRepository.findByUserIdAndMonthAndYear(user.getId(), month, year)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BudgetResponse toResponse(Budget budget) {
        Double spent = transactionRepository.sumExpensesByCategoryAndMonth(
                budget.getUser().getId(),
                budget.getCategory().getId(),
                budget.getMonth(),
                budget.getYear()
        );

        Double remaining = budget.getMonthlyLimit() - spent;

        return new BudgetResponse(
                budget.getId(),
                budget.getMonthlyLimit(),
                budget.getMonth(),
                budget.getYear(),
                budget.getCategory().getName(),
                spent,
                remaining
        );
    }
}