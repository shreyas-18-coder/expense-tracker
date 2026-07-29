package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.dto.TransactionRequest;
import com.shreyas.expensetracker.dto.TransactionResponse;
import com.shreyas.expensetracker.entity.*;
import com.shreyas.expensetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionResponse createTransaction(String userEmail, TransactionRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);

        updateAccountBalance(account, request.getType(), request.getAmount());

        return toResponse(saved);
    }

    public Page<TransactionResponse> getTransactionsForUser(String userEmail, Long categoryId,
                                                            LocalDate startDate, LocalDate endDate,
                                                            int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        return transactionRepository.findFiltered(user.getId(), categoryId, startDate, endDate, pageable)
                .map(this::toResponse);
    }

    private void updateAccountBalance(Account account, String type, Double amount) {
        if (type.equalsIgnoreCase("INCOME")) {
            account.setBalance(account.getBalance() + amount);
        } else if (type.equalsIgnoreCase("EXPENSE")) {
            account.setBalance(account.getBalance() - amount);
        }
        accountRepository.save(account);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAccount().getName(),
                transaction.getCategory().getName()
        );
    }
}