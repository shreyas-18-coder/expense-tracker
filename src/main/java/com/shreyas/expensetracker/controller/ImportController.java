package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.ImportResultResponse;
import com.shreyas.expensetracker.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping(value = "/csv", consumes = "multipart/form-data")
    public ResponseEntity<ImportResultResponse> importCsv(
            Authentication authentication,
            @RequestParam Long accountId,
            @RequestParam("file") MultipartFile file) {

        String userEmail = authentication.getName();
        ImportResultResponse result = importService.importCsv(userEmail, accountId, file);
        return ResponseEntity.ok(result);
    }
}