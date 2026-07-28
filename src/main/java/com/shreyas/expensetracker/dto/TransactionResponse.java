package com.shreyas.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Double amount;
    private String type;
    private String description;
    private LocalDate date;
    private String accountName;
    private String categoryName;
}