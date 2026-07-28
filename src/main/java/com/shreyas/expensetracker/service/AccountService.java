package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.entity.Account;
import com.shreyas.expensetracker.entity.User;
import com.shreyas.expensetracker.repository.AccountRepository;
import com.shreyas.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.shreyas.expensetracker.dto.AccountResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountResponse createAccount(String userEmail, Account account) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setUser(user);
        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    public List<AccountResponse> getAccountsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return accountRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getBalance()
        );
    }
}