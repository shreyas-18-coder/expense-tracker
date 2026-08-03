package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.dto.ImportResultResponse;
import com.shreyas.expensetracker.entity.*;
import com.shreyas.expensetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ImportResultResponse importCsv(String userEmail, Long accountId, MultipartFile file) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // skip header row
            int rowNumber = 1;

            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.isBlank()) continue;

                totalRows++;
                String[] fields = line.split(",");

                try {
                    if (fields.length < 5) {
                        throw new RuntimeException("Missing columns");
                    }

                    LocalDate date = LocalDate.parse(fields[0].trim());
                    String description = fields[1].trim();
                    Double amount = Double.parseDouble(fields[2].trim());
                    String type = fields[3].trim().toUpperCase();
                    String categoryName = fields[4].trim();

                    if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
                        throw new RuntimeException("Invalid type: " + type);
                    }

                    Category category = categoryRepository.findByUserId(user.getId()).stream()
                            .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                            .findFirst()
                            .orElseGet(() -> {
                                Category newCategory = new Category();
                                newCategory.setName(categoryName);
                                newCategory.setType(type);
                                newCategory.setUser(user);
                                return categoryRepository.save(newCategory);
                            });

                    Transaction transaction = new Transaction();
                    transaction.setDate(date);
                    transaction.setDescription(description);
                    transaction.setAmount(amount);
                    transaction.setType(type);
                    transaction.setCategory(category);
                    transaction.setAccount(account);
                    transaction.setUser(user);

                    transactionRepository.save(transaction);

                    if (type.equals("INCOME")) {
                        account.setBalance(account.getBalance() + amount);
                    } else {
                        account.setBalance(account.getBalance() - amount);
                    }
                    accountRepository.save(account);

                    successCount++;

                } catch (Exception e) {
                    errors.add("Row " + rowNumber + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }

        return new ImportResultResponse(totalRows, successCount, totalRows - successCount, errors);
    }
}
