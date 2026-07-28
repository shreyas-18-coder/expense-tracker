package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.entity.Account;
import com.shreyas.expensetracker.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.shreyas.expensetracker.dto.AccountResponse;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(Authentication authentication,
                                                         @RequestBody Account account) {
        String userEmail = authentication.getName();
        AccountResponse saved = accountService.createAccount(userEmail, account);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AccountResponse> accounts = accountService.getAccountsForUser(userEmail);
        return ResponseEntity.ok(accounts);
    }
}