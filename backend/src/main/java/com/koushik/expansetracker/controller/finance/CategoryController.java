package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.CategoryRequest;
import com.koushik.expansetracker.dto.CategoryResponse;
import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.CategoryServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInterface categoryService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CategoryRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();
        Category saved = categoryService.createCategory(mapper.toCategoryEntity(request, userId));
        return ResponseEntity.ok(mapper.toCategoryResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getUserCategories(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();
        var list = categoryService.getUserCategories(userId)
                .stream().map(mapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/with-defaults")
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesWithDefaults(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();
        var list = categoryService.getAllCategoriesIncludingDefault(userId)
                .stream().map(mapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getById(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();
        Category category = categoryService.getCategoryByIdAndUser(categoryId, userId);
        return ResponseEntity.ok(mapper.toCategoryResponse(category));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CategoryRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();
        Category updated = categoryService.updateCategory(categoryId, mapper.toCategoryEntity(request, userId), userId);
        return ResponseEntity.ok(mapper.toCategoryResponse(updated));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();
        categoryService.deleteCategorySafe(categoryId, userId);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
