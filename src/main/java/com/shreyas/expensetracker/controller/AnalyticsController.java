package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.CategoryBreakdownResponse;
import com.shreyas.expensetracker.dto.MonthlyTrendResponse;
import com.shreyas.expensetracker.dto.SummaryResponse;
import com.shreyas.expensetracker.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(
            Authentication authentication,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(analyticsService.getSummary(userEmail, month, year));
    }

    @GetMapping("/category-breakdown")
    public ResponseEntity<List<CategoryBreakdownResponse>> getCategoryBreakdown(
            Authentication authentication,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(analyticsService.getCategoryBreakdown(userEmail, month, year));
    }

    @GetMapping("/trend")
    public ResponseEntity<List<MonthlyTrendResponse>> getMonthlyTrend(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(analyticsService.getMonthlyTrend(userEmail));
    }
}