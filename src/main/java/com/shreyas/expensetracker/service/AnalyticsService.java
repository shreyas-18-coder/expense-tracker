package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.dto.CategoryBreakdownResponse;
import com.shreyas.expensetracker.dto.MonthlyTrendResponse;
import com.shreyas.expensetracker.dto.SummaryResponse;
import com.shreyas.expensetracker.entity.User;
import com.shreyas.expensetracker.repository.TransactionRepository;
import com.shreyas.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public SummaryResponse getSummary(String userEmail, Integer month, Integer year) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Double income = transactionRepository.sumByTypeAndMonth(user.getId(), "INCOME", month, year);
        Double expense = transactionRepository.sumByTypeAndMonth(user.getId(), "EXPENSE", month, year);

        return new SummaryResponse(income, expense, income - expense);
    }

    public List<CategoryBreakdownResponse> getCategoryBreakdown(String userEmail, Integer month, Integer year) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Object[]> rows = transactionRepository.getCategoryBreakdown(user.getId(), month, year);

        return rows.stream()
                .map(row -> new CategoryBreakdownResponse((String) row[0], (Double) row[1]))
                .toList();
    }

    public List<MonthlyTrendResponse> getMonthlyTrend(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Object[]> rows = transactionRepository.getMonthlyTrend(user.getId());

        return rows.stream()
                .map(row -> new MonthlyTrendResponse((Integer) row[0], (Integer) row[1], (Double) row[2]))
                .toList();
    }
}