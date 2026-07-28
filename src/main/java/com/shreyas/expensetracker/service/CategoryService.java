package com.shreyas.expensetracker.service;

import com.shreyas.expensetracker.dto.CategoryResponse;
import com.shreyas.expensetracker.entity.Category;
import com.shreyas.expensetracker.entity.User;
import com.shreyas.expensetracker.repository.CategoryRepository;
import com.shreyas.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponse createCategory(String userEmail, Category category) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        category.setUser(user);
        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    public List<CategoryResponse> getCategoriesForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return categoryRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }
}