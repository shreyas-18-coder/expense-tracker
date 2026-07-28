package com.shreyas.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Type is required")
    private String type; // INCOME or EXPENSE

    private String description;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}