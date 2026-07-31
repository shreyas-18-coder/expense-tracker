package com.shreyas.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetResponse {
    private Long id;
    private Double monthlyLimit;
    private Integer month;
    private Integer year;
    private String categoryName;
    private Double amountSpent;
    private Double remaining;
}