package com.shreyas.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetRequest {

    @NotNull(message = "Monthly limit is required")
    private Double monthlyLimit;

    @NotNull(message = "Month is required")
    private Integer month;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}