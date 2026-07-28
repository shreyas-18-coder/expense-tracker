package com.shreyas.expensetracker.controller;

import com.shreyas.expensetracker.dto.CategoryResponse;
import com.shreyas.expensetracker.entity.Category;
import com.shreyas.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(Authentication authentication,
                                                           @RequestBody Category category) {
        String userEmail = authentication.getName();
        CategoryResponse saved = categoryService.createCategory(userEmail, category);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getMyCategories(Authentication authentication) {
        String userEmail = authentication.getName();
        List<CategoryResponse> categories = categoryService.getCategoriesForUser(userEmail);
        return ResponseEntity.ok(categories);
    }
}