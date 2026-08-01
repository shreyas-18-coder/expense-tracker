package com.shreyas.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryBreakdownResponse {
    private String categoryName;
    private Double totalSpent;
}