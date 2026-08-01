package com.shreyas.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyTrendResponse {
    private Integer month;
    private Integer year;
    private Double totalSpent;
}