package com.shreyas.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportResultResponse {
    private int totalRows;
    private int successCount;
    private int failedCount;
    private List<String> errors;
}